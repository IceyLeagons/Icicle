package net.iceyleagons.gradle;

import com.amihaiemil.eoyaml.*;
import lombok.val;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ExternalModuleDependency;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.file.DuplicatesStrategy;
import org.gradle.api.internal.file.copy.CopySpecInternal;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.jvm.tasks.Jar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class IciclePlugin
        implements Plugin<Project> {
    public void apply(Project target) {
        target.getPluginManager().apply("java");
        IcicleAddonData addonData = setupAddonData(target);
        Configuration icicleConfiguration = setupDependencies(target, addonData);
        setupShadow(target, icicleConfiguration);
        setupIcicleGeneration(target, addonData);
        setupRepository(target, addonData);
        setupUTF8(target);
        setupPluginYML(target);
    }

    private void setupPluginYML(Project target) {
        target.getTasks().getByName("processResources").doFirst(task -> {
            try {
                File file = target.file("src/main/resources/plugin.yml");
                if (file.exists()) {
                    YamlMappingBuilder yamlBuilder = Yaml.createYamlMappingBuilder();
                    YamlMapping yaml = Yaml.createYamlInput(file).readYamlMapping();
                    YamlSequence sequence = yaml.yamlSequence("depend");
                    for (YamlNode key : yaml.keys()) {
                        if (key.toString().equals("depend")) continue;
                        yamlBuilder = yamlBuilder.add(key, yaml.value(key));
                    }

                    if (sequence != null) {
                        YamlSequenceBuilder sequenceBuilder = Yaml.createYamlSequenceBuilder();
                        boolean found = false;
                        for (int i = 0; i < sequence.size(); ++i) {
                            if (!sequence.string(i).equalsIgnoreCase("icicle")) continue;
                            found = true;
                        }

                        if (found)
                            for (YamlNode yamlNode : sequence)
                                sequenceBuilder = sequenceBuilder.add(yamlNode);
                        else {
                            for (YamlNode yamlNode : sequence)
                                sequenceBuilder = sequenceBuilder.add(yamlNode);
                            sequenceBuilder = sequenceBuilder.add("Icicle");
                        }
                        yamlBuilder = yamlBuilder.add("depend", sequenceBuilder.build());
                    } else
                        yamlBuilder = yamlBuilder.add("depend", Yaml.createYamlSequenceBuilder().add("Icicle").build());

                    if (!file.delete() || !file.createNewFile()) return;
                    try (FileWriter fw = new FileWriter(file)) {
                        fw.write(yamlBuilder.build().toString());
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    private void setupUTF8(Project target) {
        target.getTasks().getByName("compileJava").doFirst(task -> {
            JavaCompile javaCompile = (JavaCompile) task;
            javaCompile.getOptions().setEncoding("UTF-8");
        });
    }

    private void setupShadow(Project target, Configuration icicle) {
        Configuration icicleShadow = target.getConfigurations().create("icicleShadow");
        icicle.extendsFrom(icicleShadow);
        Configuration shadow = target.getConfigurations().create("shadow");
        target.getTasks().getByName("jar").doFirst(task -> {
            Jar jar = (Jar) task;
            ArrayList<Object> files = new ArrayList<>(20);

            for (File file : shadow)
                if (file.isDirectory()) files.add(file);
                else files.add(target.zipTree(file));

            for (File file : icicleShadow)
                if (file.isDirectory()) files.add(file);
                else files.add(target.zipTree(file));

            jar.setDuplicatesStrategy(DuplicatesStrategy.INCLUDE);
            jar.from(files.toArray(Object[]::new));
        });
        target.getConfigurations().getByName("implementation").extendsFrom(shadow);
    }

    private Configuration setupDependencies(Project target, IcicleAddonData addonData) {
        Configuration implementation = target.getConfigurations().getByName("implementation");
        Configuration icicle = target.getConfigurations().create("icicle", files -> files.withDependencies(dependencies -> {
            ArrayList<Dependency> newDependencies = new ArrayList<>(5);
            for (Dependency dep : dependencies) {
                ExternalModuleDependency dependency = (ExternalModuleDependency) dep;

                String dependencyGroup = dependency.getGroup();
                String dependencyVersion = dependency.getVersion();
                if ("icicle".equals(dependencyGroup)) {
                    String dependencyName = dependency.getName();
                    if (dependencyVersion == null) {
                        addonData.getDependencies().add(dependencyName);
                        newDependencies.add(target.getDependencies().create("net.iceyleagons:icicle-addon-" + dependencyName));
                        continue;
                    }
                    addonData.getDependencies().add(dependencyName + ":" + dependencyVersion);
                    newDependencies.add(target.getDependencies().create("net.iceyleagons:icicle-addon-" + dependencyName + ":" + dependencyVersion));
                    continue;
                }
                if (dependencyVersion == null)
                    addonData.getDependencies().add(dependency.getName());
                else addonData.getDependencies().add(dependency.getName() + ":" + dependencyVersion);
                newDependencies.add(dependency);
            }

            dependencies.clear();
            dependencies.addAll(newDependencies);
        }));

        implementation.extendsFrom(icicle);
        return icicle;
    }

    private void setupRepository(Project target, IcicleAddonData addonData) {
        target.getRepositories().add(iglooReleases(target));
        if (addonData.isSnapshots())
            target.getRepositories().add(iglooSnapshots(target));
    }

    private IcicleAddonData setupAddonData(Project target) {
        return target.getExtensions().create("icicle", IcicleAddonData.class);
    }

    private void setupIcicleGeneration(Project target, IcicleAddonData addonData) {
        val generateIcicleConfig = target.getTasks().register("generateIcicle", IcicleConfigTask.class, task -> {
            task.setGroup("Icicle addon development");
            task.setDescription("Generates the icicle.yml file for the provided addon.");
            task.setData(addonData);
            task.getOutputDirectory().set(target.getLayout().getBuildDirectory().dir(task.getName()));
        });

        target.getTasks().named("processResources", Copy.class, task -> {
            val copyIcicleFile = task.getRootSpec().addChild();
            copyIcicleFile.into("/");
            copyIcicleFile.from(generateIcicleConfig);
        });
    }

    public MavenArtifactRepository iglooSnapshots(Project target) {
        return target.getRepositories().maven(action -> {
            action.setName("Igloo Snapshots");
            try {
                action.setUrl(new URI("https://mvn.iceyleagons.net/snapshots/"));
            } catch (URISyntaxException e) {
                throw new RuntimeException("URI creation failed.");
            }
        });
    }

    public MavenArtifactRepository iglooReleases(Project target) {
        return target.getRepositories().maven(action -> {
            action.setName("Igloo Releases");
            try {
                action.setUrl(new URI("https://mvn.iceyleagons.net/releases/"));
            } catch (URISyntaxException e) {
                throw new RuntimeException("URI creation failed.");
            }
        });
    }
}
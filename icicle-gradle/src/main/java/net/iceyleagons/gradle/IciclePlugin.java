package net.iceyleagons.gradle;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMappingBuilder;
import com.amihaiemil.eoyaml.YamlNode;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ExternalModuleDependency;
import org.gradle.api.file.DuplicatesStrategy;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.jvm.tasks.Jar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class IciclePlugin implements Plugin<Project> {
    public void apply(Project target) {
        // Apply java plugin for "default" tasks and extensions.
        target.getPluginManager().apply("java");

        val addonData = setupAddonData(target);

        // Dependency types: icicle, icicleShadow, shadow, runtimeDownload
        val icicleConfiguration = setupDependencies(target, addonData);
        setupShadow(target, icicleConfiguration);
        setupRuntimeDownload(target, addonData);

        // Setup dependencies: #igloo(), #iglooSnapshots(), #sonatype(), #codemc(), #paper(), #spigot()
        setupRepository(target);

        setupDependencyAliases(target);

        // Icicle.yml generation setup
        setupIcicleGeneration(target, addonData);

        // Set the project encoding to UTF-8 & Minecraft-specific: modify plugin.yml to include Icicle in it's dependencies
        setupUTF8(target);
        setupPluginYML(target);
    }

    private void setupPluginYML(Project target) {
        target.getTasks().getByName("processResources").doFirst(task -> {
            try {
                val pluginYml = target.file("src/main/resources/plugin.yml");
                if (pluginYml.exists()) {
                    val yamlBuilder = editPluginYml(pluginYml);

                    if (!pluginYml.delete() || !pluginYml.createNewFile()) return;
                    try (FileWriter fw = new FileWriter(pluginYml)) {
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
            val javaCompile = (JavaCompile) task;
            javaCompile.getOptions().setEncoding("UTF-8");
        });
    }

    private void setupRuntimeDownload(Project target, IcicleAddonData addonData) {
        val runtime = target.getConfigurations().create("runtimeDownload", files -> files.withDependencies(dependencies -> {
            for (Dependency dep : dependencies) {
                val dependency = (ExternalModuleDependency) dep;

                val dependencyGroup = dependency.getGroup();
                val dependencyVersion = dependency.getVersion();
                val dependencyName = dependency.getName();

                if (dependencyVersion == null)
                    addonData.getRuntimeDownloads().add(dependencyGroup + ":" + dependencyName);
                else
                    addonData.getRuntimeDownloads().add(dependencyGroup + ":" + dependencyName + ":" + dependencyVersion);
            }
        }));

        target.getConfigurations().getByName("implementation").extendsFrom(runtime);
    }

    private void setupShadow(Project target, Configuration icicle) {
        val icicleShadow = target.getConfigurations().create("icicleShadow");
        val shadow = target.getConfigurations().create("shadow");

        target.getTasks().getByName("jar").doFirst(task -> {
            val jar = (Jar) task;
            val files = new ArrayList<>(20);

            for (File file : shadow)
                if (file.isDirectory()) files.add(file);
                else files.add(target.zipTree(file));

            for (File file : icicleShadow)
                if (file.isDirectory()) files.add(file);
                else files.add(target.zipTree(file));

            jar.setDuplicatesStrategy(DuplicatesStrategy.INCLUDE);
            jar.from(files.toArray());
        });

        icicle.extendsFrom(icicleShadow);
        target.getConfigurations().getByName("implementation").extendsFrom(shadow);
    }

    private Configuration setupDependencies(Project target, IcicleAddonData addonData) {
        val implementation = target.getConfigurations().getByName("implementation");
        val icicle = target.getConfigurations().create("icicle", files -> files.withDependencies(dependencies -> {
            val newDependencies = new ArrayList<Dependency>(5);
            for (Dependency dep : dependencies) {
                val dependency = (ExternalModuleDependency) dep;

                val dependencyGroup = dependency.getGroup();
                val dependencyVersion = dependency.getVersion();
                if ("icicle".equals(dependencyGroup)) {
                    val dependencyName = dependency.getName();
                    if (dependencyVersion == null) {
                        addonData.getDependencies().add(dependencyName);
                        newDependencies.add(target.getDependencies().create("net.iceyleagons:icicle-addon-" + dependencyName));
                    } else {
                        addonData.getDependencies().add(dependencyName + ":" + dependencyVersion);
                        newDependencies.add(target.getDependencies().create("net.iceyleagons:icicle-addon-" + dependencyName + ":" + dependencyVersion));
                    }
                } else {
                    if (dependencyVersion == null)
                        addonData.getDependencies().add(dependency.getName());
                    else addonData.getDependencies().add(dependency.getName() + ":" + dependencyVersion);
                    newDependencies.add(dependency);
                }
            }

            dependencies.clear();
            dependencies.addAll(newDependencies);
        }));

        implementation.extendsFrom(icicle);
        return icicle;
    }

    private void setupRepository(Project target) {
        IcicleRepositoryHelper.setInstance(new IcicleRepositoryHelper.IcicleRepository(target.getRepositories()));
    }

    private void setupDependencyAliases(Project target) {
        IcicleDependencyHelper.setInstance(new IcicleDependencyHelper.IcicleDependencies(target.getDependencies()));
    }

    private IcicleAddonData setupAddonData(Project target) {
        return (IcicleAddonData) target.getExtensions().create("icicle", IcicleAddonData.class);
    }

    private void setupIcicleGeneration(Project target, IcicleAddonData addonData) {
        val generateIcicleConfig = target.getTasks().register("generateIcicle", IcicleConfigTask.class, task -> {
            task.setGroup("Icicle addon development");
            task.setDescription("Generates the icicle.yml file for the provided addon.");

            // Populate the addonData with required information
            if (addonData.getName() == null)
                addonData.setName(target.getName());
            if (addonData.getVersion() == null)
                addonData.setVersion(target.getVersion().toString());
            if (addonData.getDependencyNotation() == null)
                addonData.setDependencyNotation("net.iceyleagons:icicle-addon-" + addonData.getName().toLowerCase().replace(' ', '-') + ":" + addonData.getVersion());

            task.setData(addonData);
            task.getOutputDirectory().set(target.getLayout().getBuildDirectory().dir(task.getName()));
        });

        target.getTasks().named("processResources", Copy.class, task -> {
            val copyIcicleFile = task.getRootSpec().addChild();
            copyIcicleFile.into("/");
            copyIcicleFile.from(generateIcicleConfig);
        });
    }

    @SneakyThrows
    private YamlMappingBuilder editPluginYml(File file) {
        var yamlBuilder = Yaml.createYamlMappingBuilder();

        val yaml = Yaml.createYamlInput(file).readYamlMapping();
        val existingDepends = yaml.yamlSequence("depend");
        // Copy over existing non-trivial stuff.
        for (YamlNode key : yaml.keys()) {
            if (key.toString().equals("depend")) continue;
            yamlBuilder = yamlBuilder.add(key, yaml.value(key));
        }

        if (existingDepends != null) {
            var sequenceBuilder = Yaml.createYamlSequenceBuilder();
            boolean found = false;
            for (int i = 0; i < existingDepends.size(); ++i) {
                if (!existingDepends.string(i).equalsIgnoreCase("icicle")) continue;
                found = true;
            }

            if (found)
                for (YamlNode yamlNode : existingDepends)
                    sequenceBuilder = sequenceBuilder.add(yamlNode);
            else {
                for (YamlNode yamlNode : existingDepends)
                    sequenceBuilder = sequenceBuilder.add(yamlNode);
                sequenceBuilder = sequenceBuilder.add("Icicle");
            }
            yamlBuilder = yamlBuilder.add("depend", sequenceBuilder.build());
        } else
            yamlBuilder = yamlBuilder.add("depend", Yaml.createYamlSequenceBuilder().add("Icicle").build());

        return yamlBuilder;
    }
}
package net.iceyleagons.gradle;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlMappingBuilder;
import com.amihaiemil.eoyaml.YamlSequenceBuilder;
import lombok.Setter;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.internal.file.Deleter;

import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;

public class IcicleConfigTask
        extends DefaultTask {
    @Setter
    private IcicleAddonData data;
    private final DirectoryProperty outputDirectory;

    public IcicleConfigTask() {
        ObjectFactory objectFactory = getProject().getObjects();
        outputDirectory = objectFactory.directoryProperty();
    }

    @OutputDirectory
    public DirectoryProperty getOutputDirectory() {
        return outputDirectory;
    }

    @Inject
    protected Deleter getDeleter() {
        throw new UnsupportedOperationException("Decorator takes care of injection");
    }

    private YamlMapping createYaml() {
        YamlSequenceBuilder sequenceBuilder;
        YamlMappingBuilder yamlBuilder = Yaml.createYamlMappingBuilder().add("name", data.getName()).add("description", data.getDescription()).add("version", data.getVersion());
        if (!data.getDevelopers().isEmpty()) {
            sequenceBuilder = Yaml.createYamlSequenceBuilder();
            for (String developer : data.getDevelopers())
                sequenceBuilder = sequenceBuilder.add(developer);
            yamlBuilder = yamlBuilder.add("developers", sequenceBuilder.build());
        }
        if (!data.getDependencies().isEmpty()) {
            sequenceBuilder = Yaml.createYamlSequenceBuilder();
            for (String dependency : data.getDependencies())
                sequenceBuilder = sequenceBuilder.add(dependency);
            yamlBuilder = yamlBuilder.add("dependencies", sequenceBuilder.build());
        }

        return yamlBuilder.build();
    }

    @TaskAction
    public void generateIcicleConfig() {
        File outputDir = outputDirectory.get().getAsFile();
        clearOutputDirectory(outputDir);
        File icicleFile = new File(outputDir, "icicle.yml");
        try (FileWriter fw = new FileWriter(icicleFile)) {
            fw.write(createYaml().toString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void clearOutputDirectory(File directoryToClear) {
        try {
            getDeleter().ensureEmptyDirectory(directoryToClear);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

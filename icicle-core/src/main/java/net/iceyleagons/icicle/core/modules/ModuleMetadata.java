package net.iceyleagons.icicle.core.modules;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import net.iceyleagons.icicle.utilities.Asserts;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@EqualsAndHashCode
public class ModuleMetadata {

    private final YamlFile yamlFile;


    public ModuleMetadata(InputStream inputStream) {
        this.yamlFile = YamlFile.loadConfiguration(inputStream);

        //GABE please fill this out, 'cause I dunno what's required/optional, and how dependencies work
    }

    @SneakyThrows
    public static ModuleMetadata fromFile(File file) {
        Asserts.notNull(file, "File must not be null!");

        try (JarFile jarFile = new JarFile(file)) {
            JarEntry metaEntry = jarFile.getJarEntry("icicle.yaml");

            if (metaEntry == null || metaEntry.isDirectory())
                throw new IllegalStateException("icicle.yaml file is not found or is a directory!");

            try (InputStream inputStream = jarFile.getInputStream(metaEntry)) {
                return new ModuleMetadata(inputStream);
            }
        }
    }
}

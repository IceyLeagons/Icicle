/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.icicle.core.modules;

import lombok.*;
import net.iceyleagons.icicle.core.utils.Version;
import net.iceyleagons.icicle.utilities.Asserts;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@EqualsAndHashCode
@RequiredArgsConstructor
@Getter
public class ModuleMetadata {

    @NonNull
    private final File baseFile;
    @NonNull
    private final String name;
    @NonNull
    private final String description;
    @NonNull
    private final DependencyNotation dependencyNotation;
    @NonNull
    private final String mainClass;
    @NonNull
    private final Version version;
    @NonNull
    private final List<String> developers;
    @NonNull
    private final List<DependencyNotation> dependencies;

    // private static final List<ModuleMetadata> loadedModules = new ArrayList<>();

    public ModuleMetadata(InputStream inputStream, File file) throws IOException {
        val yamlFile = YamlFile.loadConfiguration(inputStream);

        // Required stuff...
        val name = yamlFile.getString("name");
        val dependencyNotation = yamlFile.getString("dependency-notation");
        val mainClass = yamlFile.getString("main-class");
        val description = yamlFile.getString("description");
        val version = new Version(yamlFile.getString("version"));

        var dependencies = yamlFile.getStringList("dependencies");
        if (dependencies == null)
            dependencies = new ArrayList<>();
        // Commented out since load order isn't defined here.
        /*if (dependencies != null) {
            // Check if dependency is installed/has correct version.
            if (!dependencies.stream().allMatch(dependency -> {
                val splitDependency = dependency.split(":");
                val dependencyName = splitDependency[1];
                val dependencyVersion = new Version(splitDependency[2]);

                for (ModuleMetadata loadedDependency : loadedModules)
                    if (loadedDependency.name.equalsIgnoreCase(dependencyName))
                        if (loadedDependency.version.compareTo(dependencyVersion) >= 0)
                            return true;

                return false;
            }))
                throw new IllegalArgumentException("Required dependencies not found!");
        }*/

        var developers = yamlFile.getStringList("developers");
        if (developers == null)
            developers = new ArrayList<>();

        this.baseFile = file;
        this.name = name;
        this.version = version;
        this.developers = developers;
        this.description = description;
        this.dependencies = dependencies.stream().map(DependencyNotation::fromString).collect(Collectors.toUnmodifiableList());
        this.dependencyNotation = DependencyNotation.fromString(dependencyNotation);
        this.mainClass = mainClass;
    }

    @SneakyThrows
    public static ModuleMetadata fromFile(File file) {
        Asserts.notNull(file, "File must not be null!");

        try (JarFile jarFile = new JarFile(file)) {
            JarEntry metaEntry = jarFile.getJarEntry("icicle.yml");

            if (metaEntry == null || metaEntry.isDirectory())
                throw new IllegalStateException("icicle.yml file is not found or is a directory!");

            try (InputStream inputStream = jarFile.getInputStream(metaEntry)) {
                return new ModuleMetadata(inputStream, file);
            }
        }
    }
}

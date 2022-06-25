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

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.*;
import net.iceyleagons.icicle.core.maven.MavenDependency;
import net.iceyleagons.icicle.utilities.Asserts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@EqualsAndHashCode
@RequiredArgsConstructor
@Getter
public class ModuleMetadata {

    @NonNull
    private final File moduleFile;
    @NonNull
    private final String name;
    private final int updateType;
    @Nullable
    private final String updaterClass;
    @NonNull
    private final List<MavenDependency> dependencies;
    @NonNull
    private final List<MavenDependency> icicleDependencies;

    public ModuleMetadata(InputStream inputStream, @NotNull File file) throws IOException {
        val yamlFile = YamlFile.loadConfiguration(inputStream);

        // Required stuff...
        val name = yamlFile.getString("name");

        val updaterClass = yamlFile.getString("update-class");
        var updateType = 1;
        try {
            updateType = Integer.parseInt(yamlFile.getString("update-type"));
        } catch (Exception ignored) {

        }

        var dependencies_ = yamlFile.getMapList("dependencies");
        if (dependencies_ == null)
            dependencies_ = new ObjectArrayList<>(1);

        var icicleDependencies_ = yamlFile.getMapList("icicle-dependencies");
        if (icicleDependencies_ == null)
            icicleDependencies_ = new ObjectArrayList<>(1);

        val dependencies = new ObjectArrayList<MavenDependency>(dependencies_.size());
        for (Map<?, ?> m : dependencies_) {
            Map<String, List<String>> map = (Map<String, List<String>>) m;
            for (Map.Entry<String, List<String>> stringListEntry : map.entrySet())
                for (String dependency : stringListEntry.getValue()) {
                    String[] split = dependency.split(":");
                    dependencies.add(new MavenDependency(split[0], split[1], split[2], stringListEntry.getKey()));
                }
        }

        val icicleDependencies = new ObjectArrayList<MavenDependency>(icicleDependencies_.size());
        for (Map<?, ?> m : icicleDependencies_) {
            Map<String, List<String>> map = (Map<String, List<String>>) m;
            for (Map.Entry<String, List<String>> stringListEntry : map.entrySet())
                for (String dependency : stringListEntry.getValue()) {
                    String[] split = dependency.split(":");
                    dependencies.add(new MavenDependency(split[0], split[1], split[2], stringListEntry.getKey()));
                }
        }

        this.name = name;
        this.dependencies = dependencies;
        this.updateType = updateType;
        this.updaterClass = updaterClass;
        this.icicleDependencies = icicleDependencies;
        this.moduleFile = file;
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

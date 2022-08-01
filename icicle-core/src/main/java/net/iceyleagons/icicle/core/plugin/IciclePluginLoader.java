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

package net.iceyleagons.icicle.core.plugin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.SneakyThrows;
import net.iceyleagons.icicle.core.Icicle;
import net.iceyleagons.icicle.core.maven.AdvancedClassLoaders;
import net.iceyleagons.icicle.core.maven.MavenDependency;
import net.iceyleagons.icicle.core.maven.MavenLibraryLoader;
import net.iceyleagons.icicle.core.maven.loaders.AdvancedClassLoader;
import net.iceyleagons.icicle.utilities.Asserts;
import net.iceyleagons.icicle.utilities.datastores.tuple.UnmodifiableTuple;
import net.iceyleagons.icicle.utilities.lang.Experimental;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Set;

@Experimental
public class IciclePluginLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(IciclePluginLoader.class);

    private final AdvancedClassLoader acl;

    private final List<PluginMetadata> loadedModules = new ObjectArrayList<>(4);
    private final Set<UnmodifiableTuple<String, String>> loadedDependencies = new ObjectOpenHashSet<>(8);

    public IciclePluginLoader(ClassLoader classLoader) {
        acl = AdvancedClassLoaders.get((URLClassLoader) classLoader);
    }

    @SneakyThrows
    public void loadPluginDependencies(File jarFile) {
        PluginMetadata meta = loadModule(jarFile);

        for (MavenDependency library : meta.getDependencies()) {
            UnmodifiableTuple<String, String> depTuple = new UnmodifiableTuple<>(library.getGroupId(), library.getArtifactId());
            if (!loadedDependencies.contains(depTuple)) {
                MavenLibraryLoader.load(library);
                loadedDependencies.add(depTuple);
            }
        }

        for (MavenDependency library : meta.getIcicleDependencies()) {
            UnmodifiableTuple<String, String> depTuple = new UnmodifiableTuple<>(library.getGroupId(), library.getArtifactId());
            if (!loadedDependencies.contains(depTuple)) {
                MavenLibraryLoader.load(library);
                loadedDependencies.add(depTuple);
            }
        }

        acl.loadLibrary(meta.getModuleFile());
        Icicle.ICICLE_REFLECTIONS.merge(new Reflections(acl.getOrigin()));
        Icicle.ICICLE_REFLECTIONS.expandSuperTypes();
    }


    private PluginMetadata loadModule(File file) throws MalformedURLException {
        Asserts.notNull(file, "Module file must not be null!");
        Asserts.state(file.exists(), "Module file does not exist!");
        Asserts.isTrue(file.getName().endsWith(".jar"), "Module file is not a jar!");
        return PluginMetadata.fromFile(file);
    }
}

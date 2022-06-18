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
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.iceyleagons.icicle.core.maven.MavenLibraryLoader;
import net.iceyleagons.icicle.utilities.Asserts;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;
import net.iceyleagons.icicle.utilities.lang.Experimental;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.util.*;

@Experimental
public class IcicleModuleLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(IcicleModuleLoader.class);

    private final AdvancedFile modulesFolder;

    //This map keeps track of all loaded modules' classloaders, so when a plugin is dependent on the module
    //its reflections can use that ClassLoader as well
    private final Set<DependencyNotation> loadedDependencies = new ObjectOpenHashSet<>();
    private final List<ModuleMetadata> loadedModules = new ObjectArrayList<>();

    public IcicleModuleLoader(AdvancedFile folder) {
        Asserts.state(folder.isDirectory(), "Folder must be a folder!");

        this.modulesFolder = folder;
        loadModulesInFolder();
    }

    public void loadModulesInFolder() {
        for (File file : Objects.requireNonNull(modulesFolder.asFile().listFiles()))
            try {
                loadedModules.add(loadModule(file));
            } catch (MalformedURLException e) {
                LOGGER.warn("Could not load module from file {} due to {}", file.getPath(), e);
            }

        final List<DependencyNotation> dependencyNotations = loadedModules.stream().map(ModuleMetadata::getDependencyNotation).toList();

        for (DependencyNotation dependencyNotation : dependencyNotations) {
            if (loadedDependencies.contains(dependencyNotation)) continue;

            String groupId = dependencyNotation.getAuthor(); // Is this correct ?
            String artifactId = dependencyNotation.getName(); // Is this correct ?
            String version = dependencyNotation.getVersion().toString();

            if (groupId == null) {
                MavenLibraryLoader.load("net.iceyleagons", artifactId, version, "https://mvn.iceyleagons.net/");
                continue;
            }

            MavenLibraryLoader.load(groupId, artifactId, version); // Using central maven
        }
    }

    public ModuleMetadata loadModule(File file) throws MalformedURLException {
        Asserts.notNull(file, "Module file must not be null!");
        Asserts.state(file.exists(), "Module file does not exist!");
        Asserts.isTrue(file.getName().endsWith(".jar"), "Module file is not a jar!");
        return ModuleMetadata.fromFile(file);
    }
}

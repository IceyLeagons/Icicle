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

package net.iceyleagons.icicle.core.maven;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.core.Icicle;
import net.iceyleagons.icicle.core.maven.loaders.AdvancedClassLoader;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;
import net.iceyleagons.icicle.utilities.file.FileUtils;
import net.iceyleagons.icicle.utilities.lang.Experimental;
import net.iceyleagons.icicle.utilities.lang.Internal;
import org.reflections.Reflections;

import java.io.File;
import java.net.URLClassLoader;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 26, 2021
 */
@Internal
@Experimental
public class MavenLibraryLoader {

    public static final AdvancedFile ICICLE_LIB_FOLDER;
    public static final String MAVEN_CENTRAL_REPO = "https://repo1.maven.org/maven2";
    public static final String MAVEN_JITPACK = "https://jitpack.io";
    private static final AdvancedClassLoader acl = AdvancedClassLoaders.get((URLClassLoader) Icicle.ICICLE_CLASS_LOADER);

    static {
        ICICLE_LIB_FOLDER = new AdvancedFile(new File("icicleLibs"), true);
    }

    public static void load(String groupId, String artifactId, String version) {
        load(groupId, artifactId, version, MAVEN_CENTRAL_REPO); //central maven
    }

    public static void load(String groupId, String artifactId, String version, String repo) {
        load(new MavenDependency(groupId, artifactId, version, repo));
    }

    @SneakyThrows
    public static void load(MavenDependency dependency) {
        File f = ICICLE_LIB_FOLDER.getChild(dependency.getName() + ".jar");

        if (!f.exists()) FileUtils.downloadTo(f, dependency.getRequestUrl());
        if (!f.exists()) {
            throw new IllegalStateException("Unable to download maven dependency: " + dependency.getName());
        }

        acl.loadLibrary(f);
        Icicle.ICICLE_REFLECTIONS.merge(new Reflections(acl.getOrigin()));
        Icicle.ICICLE_REFLECTIONS.expandSuperTypes();
    }
}

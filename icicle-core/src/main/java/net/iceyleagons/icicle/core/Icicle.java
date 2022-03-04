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

package net.iceyleagons.icicle.core;


import net.iceyleagons.icicle.core.maven.MavenDependency;
import net.iceyleagons.icicle.core.maven.MavenLibraryLoader;
import net.iceyleagons.icicle.core.proxy.ByteBuddyProxyHandler;
import org.reflections.Reflections;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Main class of Icicle.
 *
 * @author TOTHTOMI
 * @version 1.1.0
 * @since Aug. 23, 2021
 */
public class Icicle {

    public static final String ICICLE_VERSION = "1.0.0";

    public static final boolean PERFORMANCE_DEBUG = true;
    public static final ClassLoader ICICLE_CLASS_LOADER = URLClassLoader.newInstance(new URL[0], Icicle.class.getClassLoader());

    // In newer version of Java, the default class loader is AppClassLoader, which cannot be cast to URLClassLoader, so we do it this way:
    public static final ClassLoader[] ICICLE_CLASS_LOADERS = new ClassLoader[]{ICICLE_CLASS_LOADER};


    public static final Reflections ICICLE_REFLECTIONS = new Reflections("net.iceyleagons.icicle", ICICLE_CLASS_LOADERS);
    // TODO Gradle plugin --> icicle.yml and the core searches for its dependencies rather than this \/
    public static final MavenDependency[] CORE_DEPENDENCIES = new MavenDependency[]{
            new MavenDependency("net.bytebuddy", "byte-buddy", "1.11.15", MavenLibraryLoader.MAVEN_CENTRAL_REPO),
            new MavenDependency("net.bytebuddy", "byte-buddy-agent", "1.11.15", MavenLibraryLoader.MAVEN_CENTRAL_REPO),
            new MavenDependency("me.carleslc.Simple-YAML", "Simple-Yaml", "1.7.2", MavenLibraryLoader.MAVEN_JITPACK),
            new MavenDependency("ch.qos.logback", "logback-core", "1.2.9", MavenLibraryLoader.MAVEN_CENTRAL_REPO)
    };
    public static boolean LOADED = false;

    public static String getCopyrightText() {
        return "Icicle is licensed under the terms of MIT License.";
    }

    public static String getLoadText() {
        return String.format("Loading Icicle v%s. %s", ICICLE_VERSION, getCopyrightText());
    }

    public static void loadIcicle() {
        if (LOADED) {
            throw new IllegalStateException("Icicle is already loaded!");
        }

        System.out.println();
        System.out.println("[==================[Icicle Loader]==================]");
        System.out.println();

        System.out.printf("[Icicle] - %s", getLoadText());
        System.out.println("[Icicle] - Downloading & loading core libraries... (This may take a while)");
        for (MavenDependency coreDependency : CORE_DEPENDENCIES) {
            MavenLibraryLoader.load(coreDependency);
        }
        System.out.println("[Icicle] - Libraries loaded!");
        ByteBuddyProxyHandler.installBuddyAgent();

        System.out.println();
        System.out.println("[========================[ ]========================]");
        System.out.println();

        LOADED = true;
    }
}

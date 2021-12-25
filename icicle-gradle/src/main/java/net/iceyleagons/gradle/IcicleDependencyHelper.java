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

package net.iceyleagons.gradle;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;

public class IcicleDependencyHelper {

    @Setter
    private static IcicleDependencies instance;

    public static Dependency lombok() {
        return instance.lombok();
    }

    public static Dependency icicleCore() {
        return instance.icicleCore();
    }

    public static Dependency spigotApi() {
        return instance.spigotApi("1.17.1");
    }

    public static Dependency spigotApi(String version) {
        return instance.spigotApi(version);
    }

    public static Dependency paperApi() {
        return instance.paperApi("1.17.1");
    }

    public static Dependency paperApi(String version) {
        return instance.paperApi(version);
    }

    public static Dependency nms() {
        return instance.nms("1.17.1");
    }

    public static Dependency nms(String version) {
        return instance.nms(version);
    }

    @AllArgsConstructor
    public static class IcicleDependencies {
        private final DependencyHandler dependencyHandler;

        public Dependency lombok() {
            dependencyHandler.add("annotationProcessor", "org.projectlombok:lombok:1.18.22");
            return dependencyHandler.add("compileOnly", "org.projectlombok:lombok:1.18.22");
        }

        public Dependency icicleCore() {
            return dependencyHandler.add("implementation", "net.iceyleagons:icicle-core:1.0.0");
        }

        public Dependency spigotApi(String version) {
            return dependencyHandler.add("implementation", "org.spigotmc:spigot-api:" + version + "-R0.1-SNAPSHOT");
        }

        public Dependency paperApi(String version) {
            Version version1 = new Version(version);

            if (version1.compareTo(new Version("1.17.0")) > 0)
                return dependencyHandler.add("implementation", "io.papermc.paper:paper-api:" + version + "-R0.1-SNAPSHOT");
            else
                return dependencyHandler.add("implementation", "com.destroystokyo.paper:paper-api:" + version + "-R0.1-SNAPSHOT");
        }

        public Dependency nms(String version) {
            return dependencyHandler.add("implementation", "org.spigotmc:spigot:" + version + "-R0.1-SNAPSHOT");
        }
    }

    public static class Version implements Comparable<Version> {
        @NonNull
        public final int[] numbers;

        public Version(@NonNull String version) {
            final String[] split = version.split("-")[0].split("\\.");
            numbers = new int[split.length];
            for (int i = 0; i < split.length; i++)
                numbers[i] = Integer.parseInt(split[i]);
        }

        @Override
        public int compareTo(@NonNull Version another) {
            final int maxLength = Math.max(numbers.length, another.numbers.length);
            for (int i = 0; i < maxLength; i++) {
                final int left = i < numbers.length ? numbers[i] : 0;
                final int right = i < another.numbers.length ? another.numbers[i] : 0;
                if (left != right) {
                    return left < right ? -1 : 1;
                }
            }
            return 0;
        }
    }
}

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
import lombok.Getter;
import lombok.Setter;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;

import java.net.URI;

public class IcicleRepositoryHelper {

    @Setter
    private static IcicleRepository instance;

    public static MavenArtifactRepository igloo() {
        return instance.igloo();
    }

    public static MavenArtifactRepository iglooSnapshots() {
        return instance.iglooSnapshots();
    }

    public static MavenArtifactRepository sonatype() {
        return instance.sonatype();
    }

    public static MavenArtifactRepository codemc() {
        return instance.codemc();
    }

    public static MavenArtifactRepository spigot() {
        return instance.spigot();
    }

    public static MavenArtifactRepository paper() {
        return instance.paper();
    }

    public static MavenArtifactRepository jitpack() {
        return instance.jitpack();
    }

    @AllArgsConstructor
    @Getter
    public static class IcicleRepository {
        private final RepositoryHandler repositoryHandler;

        protected MavenArtifactRepository igloo() {
            return repositoryHandler.maven(repo -> {
                repo.setName("Icicle Igloo");
                repo.setUrl(URI.create("https://mvn.iceyleagons.net/releases/"));
            });
        }

        protected MavenArtifactRepository iglooSnapshots() {
            return repositoryHandler.maven(repo -> {
                repo.setName("Icicle Igloo Snapshots");
                repo.setUrl(URI.create("https://mvn.iceyleagons.net/snapshots/"));
            });
        }

        protected MavenArtifactRepository sonatype() {
            return repositoryHandler.maven(repo -> {
                repo.setName("Sonatype Snapshots");
                repo.setUrl(URI.create("https://oss.sonatype.org/content/repositories/snapshots/"));
            });
        }

        protected MavenArtifactRepository codemc() {
            return repositoryHandler.maven(repo -> {
                repo.setName("CodeMC repo");
                repo.setUrl(URI.create("https://repo.codemc.io/repository/nms/"));
            });
        }

        protected MavenArtifactRepository spigot() {
            return repositoryHandler.maven(repo -> {
                repo.setName("Spigot Snapshots");
                repo.setUrl(URI.create("https://hub.spigotmc.org/nexus/content/repositories/snapshots/"));
            });
        }

        protected MavenArtifactRepository paper() {
            return repositoryHandler.maven(repo -> {
                repo.setName("Paper Public");
                repo.setUrl(URI.create("https://papermc.io/repo/repository/maven-public/"));
            });
        }

        protected MavenArtifactRepository jitpack() {
            return repositoryHandler.maven(repo -> {
                repo.setName("Jitpack");
                repo.setUrl(URI.create("https://jitpack.io/"));
            });
        }

    }

}

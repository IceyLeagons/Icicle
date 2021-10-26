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

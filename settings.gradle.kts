pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            url = uri("https://mvn.iceyleagons.net/snapshots/")
            name = "Igloo Snapshots"
        }
    }
}

rootProject.name = "icicle"

include("icicle-core")
include("icicle-utilities")
include("icicle-serialization")
include("icicle-gradle")

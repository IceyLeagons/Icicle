plugins {
    java
    id("net.iceyleagons.icicle-gradle") version "1.5-SNAPSHOT"
}

version = "0.1-SNAPSHOT"
val spigotVersion = "1.17.1"

repositories {
    mavenCentral()
    spigot()
    jitpack()
}

dependencies {
    lombok()
    // https://mvnrepository.com/artifact/io.netty/netty-all
    implementation("io.netty:netty-all:4.1.72.Final")

    implementation(project(":icicle-core"))
    implementation(project(":icicle-utilities"))
    spigotApi(spigotVersion)
}

icicle {
    name = "Minecraft NMS"

    dependencyNotation = "net.iceyleagons:icicle-addon-nms:$version"
    description =
        "Contains wrapped representations of NMS classes."
    version = project.version.toString()
    developers = listOf("TOTHTOMI")

    dependencies += "net.iceyleagons:icicle-addon-core:0.1-SNAPSHOT"
    dependencies += "net.iceyleagons:icicle-addon-utilities:0.1-SNAPSHOT"
}

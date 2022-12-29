plugins {
    id("java")
    id("maven-publish")
    alias(libs.plugins.icicle)
}

group = "net.iceyleagons"
version = "unspecified"

repositories {
    mavenCentral()
    repos.jitpack
}

dependencies {

    implementation(project(":icicle-core"))
    implementation(project(":icicle-utilities"))
    implementation(project(":icicle-serialization"))
    compileOnly(libs.reflections)
    compileOnly(libs.fastutil)
    compileOnly(libs.jetbrainsannotations)
    compileOnly(libs.slf4j)

    implementation("net.dv8tion:JDA:5.0.0-beta.2") {
        exclude(module = "opus-java")
    }

    // compileOnly(minecraft.spigotApi(libs.versions.spigot.get()))
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)


    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

publishing {
    repositories {
        maven {
            credentials {
                username = project.properties["ilSnapshotUser"].toString()
                password = project.properties["ilSnapshotPwd"].toString()
            }
            url = uri("https://mvn.iceyleagons.net/snapshots")
        }
    }
    publications {
        create<MavenPublication>("Maven") {
            groupId = "net.iceyleagons"
            artifactId = "icicle-jda"
            version = "1.0.0-SNAPSHOT"
            from(components["java"])
        }
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
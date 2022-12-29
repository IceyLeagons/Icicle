plugins {
    id("java")
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

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
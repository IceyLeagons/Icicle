plugins {
    id("java")
    idea
    id("org.jetbrains.intellij") version "1.8.0"
    id("org.jetbrains.grammarkit") version "2021.2.2"
}


group = "org.example"
version = "unspecified"

intellij {
    version.set("2021.3.3")
    type.set("IC")
    plugins.set(listOf("IntelliLang", "java"))
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

sourceSets["main"].java.srcDirs("src/main/gen")
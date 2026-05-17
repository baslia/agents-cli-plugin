import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    kotlin("jvm") version "1.9.24"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.adelbasli"
version = "0.1.0"

repositories {
    mavenCentral()
}

intellij {
    version.set("2024.1.7")
    type.set("PY")
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("241")
        untilBuild.set("241.*")
    }
}

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

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(26))
    }
}

intellij {
    version.set("2026.1.2")
    type.set("PY")
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "26"
        targetCompatibility = "26"
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "26"
    }

    patchPluginXml {
        sinceBuild.set("261")
        untilBuild.set("261.*")
    }
}

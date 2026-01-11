@file:Suppress("UnstableApiUsage", "SpellCheckingInspection")

plugins {
    alias(libs.plugins.shadow)
}

architectury { neoForge() }

val shadowBundle: Configuration by configurations.getting
val developmentNeoForge: Configuration by configurations.getting
configurations {
    developmentNeoForge.extendsFrom(common.get())
}

repositories {
    maven("https://maven.neoforged.net/releases") { name = "NeoForged" }
}

dependencies {
    neoForge(libs.neoforge.neoforge)
}

tasks {
    shadowJar {
        configurations = listOf(shadowBundle)
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        inputFile.set(shadowJar.flatMap { it.archiveFile })
        dependsOn(shadowJar)
    }
}

@file:Suppress("UnstableApiUsage", "SpellCheckingInspection")

plugins {
    alias(libs.plugins.shadow)
}

architectury { fabric() }

val shadowBundle: Configuration by configurations.getting
val developmentFabric: Configuration by configurations.getting
configurations {
    developmentFabric.extendsFrom(common.get())
}

repositories {
    maven("https://maven.terraformersmc.com/") { name = "Terraformers" }  // ModMenu
}

dependencies {
    modImplementation(libs.fabric.loader)

    modLocalRuntime(libs.fabric.api)
    modLocalRuntime(libs.fabric.modmenu)
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

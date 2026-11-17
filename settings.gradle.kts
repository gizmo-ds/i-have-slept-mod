@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") { name = "FabricMC" }
        maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version providers.gradleProperty("stonecutter")
    id("dev.kikugie.loom-back-compat") version providers.gradleProperty("loom-back-compat")
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

stonecutter {
    create(rootProject) {
        // Registers one "<version>-<loader>" node per loader, each using build.<loader>.gradle.kts.
        fun match(project: String, vararg loaders: String, version: String = project) {
            for (loader in loaders) version("$project-$loader", version).buildscript("build.$loader.gradle.kts")
        }

        match("26.4", "fabric", version = "26.4-snapshot-7")
        match("26.3", "fabric", "neoforge")
        match("26.2", "fabric", "neoforge")
        match("26.1.x", "fabric", "neoforge", version = "26.1.2")
        match("1.21.11", "fabric", "neoforge")
        match("1.21.4", "fabric", "neoforge")
        match("1.21.1", "fabric", "neoforge")

        vcsVersion = "26.3-fabric"
    }
}

rootProject.name = "i_have_slept"
includeBuild("build-logic")

// loom-back-compat looks up the Loom plugin version in each project's extra properties before
// the project is evaluated, so copy the values Stonecutter loaded into the root project.
gradle.lifecycle.beforeProject {
    if (path == ":") return@beforeProject
    listOf("loomx.loom_version", "loomx.loom_unobf_plugin", "loomx.loom_remap_plugin").forEach { key ->
        rootProject.findProperty(key)?.let { extra[key] = it }
    }
}
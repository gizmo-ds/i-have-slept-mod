@file:Suppress("UnstableApiUsage", "SpellCheckingInspection")

plugins {
    alias(libs.plugins.shadow)
}

architectury { forge() }

loom {
    forge {
        mixinConfig("i_have_slept.mixins.json")
    }
}

val shadowBundle: Configuration by configurations.getting
val developmentForge: Configuration by configurations.getting
configurations {
    developmentForge.extendsFrom(common.get())
}

repositories {}

dependencies {
    forge(libs.forge.forge)
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

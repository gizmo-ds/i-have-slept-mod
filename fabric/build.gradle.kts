@file:Suppress("UnstableApiUsage", "SpellCheckingInspection")

plugins {
    id("com.github.johnrengelman.shadow")
}

apply(plugin = "com.hypherionmc.modutils.modpublisher")

architectury { fabric() }

fun prop(name: String) = rootProject.property(name).toString()
val shadowBundle: Configuration by configurations.getting
val developmentFabric: Configuration by configurations.getting
configurations {
    developmentFabric.extendsFrom(common.get())
}

repositories {
    maven("https://maven.terraformersmc.com/") { name = "Terraformers" }  // ModMenu
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${prop("fabric_loader_version")}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("fabric_api_version")}")

    modLocalRuntime("com.terraformersmc:modmenu:13.0.3")
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filteringCharset = "UTF-8"

        filesMatching("fabric.mod.json") { expand("version" to project.version) }
    }

    shadowJar {
        configurations = listOf(shadowBundle)
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        inputFile.set(shadowJar.flatMap { it.archiveFile })
        dependsOn(shadowJar)
    }

    publisher {
        artifact.set(remapJar)

        modrinthDepends {
            required("fabric-api")
        }
        curseDepends {
            required("fabric-api")
        }
    }
}

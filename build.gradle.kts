@file:Suppress("UnstableApiUsage", "SpellCheckingInspection")

import com.hypherionmc.modpublisher.plugin.ModPublisherGradleExtension
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.kotlin.dsl.add

plugins {
    java
    id("dev.architectury.loom") version "1.10-SNAPSHOT" apply false
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
    id("co.uzzu.dotenv.gradle") version "4.0.0"
    id("com.hypherionmc.modutils.modpublisher") version "2.1.6" apply false
}

fun prop(name: String) = rootProject.property(name).toString()
val mcVersion: String = prop("minecraft_version")
val curseforgeToken: String = env.fetch("CF_TOKEN", "").trim()
val modrinthToken: String = env.fetch("MODRINTH_TOKEN", "").trim()
val modChangelog = rootProject.file("CHANGELOG.md").readText().split("###")[1].let { x -> "###$x".trim() }

architectury { minecraft = mcVersion }

allprojects {
    group = prop("maven_group")
    version = "${prop("mod_version")}-$mcVersion"
}

subprojects {
    apply(plugin = "architectury-plugin")
    apply(plugin = "dev.architectury.loom")

    base.archivesName.set("${prop("archives_name")}-${project.name}")

    var mappingsDependency: Dependency? = null
    configure<LoomGradleExtensionAPI> {
        silentMojangMappingsLicense()

        mappingsDependency = layered {
            officialMojangMappings()
            parchment("org.parchmentmc.data:parchment-$mcVersion:${prop("parchment")}@zip")
        }
    }

    repositories {
        maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
    }

    dependencies {
        "minecraft"("net.minecraft:minecraft:$mcVersion")
        mappingsDependency?.let { "mappings"(it) }
    }

    java {
        withSourcesJar()

        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
    tasks.named("clean") {
        doLast { delete("logs") }
    }
}

configure(prop("pub.enabled_platforms").split(",").map { project(":$it") }) {
    apply(plugin = "architectury-plugin")
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "com.hypherionmc.modutils.modpublisher")

    val platformName = project.extensions.getByName<LoomGradleExtensionAPI>("loom")
        .platform.map { it.displayName() }.get()

    configure<dev.architectury.plugin.ArchitectPluginExtension> {
        platformSetupLoomIde()
    }

    val common: Configuration by configurations.creating
    val shadowBundle: Configuration by configurations.creating
    configurations {
        compileOnly.configure { extendsFrom(common) }
        runtimeOnly.configure { extendsFrom(common) }

        shadowBundle.isCanBeResolved = true
        shadowBundle.isCanBeConsumed = false
    }

    dependencies {
        common(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
        shadowBundle(project(path = ":common", configuration = "transformProduction$platformName"))
    }

    tasks.processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        from(rootProject.file("LICENSE")) { rename { "LICENSE.txt" } }
        from(rootProject.file("third-party-licenses")) { into("third-party-licenses") }
        from(project.file("third-party-licenses")) { into("third-party-licenses") }
        from(rootProject.file("assets/logo.png")) { rename { "${prop("archives_name")}_logo.png" } }
        from(rootProject.file("assets/private-logo.png")) { rename { "${prop("archives_name")}_logo.png" } }
    }

    configure<ModPublisherGradleExtension> {
        apiKeys {
            modrinth(modrinthToken)
            curseforge(curseforgeToken)
        }

        modrinthID.set(prop("pub.modrinth_id"))
        curseID.set(prop("pub.curseforge_id"))

        debug.set(prop("pub.debug").toBoolean())

        versionType.set("release")
        changelog.set(modChangelog)
        displayName.set("I have slept ${prop("mod_version")} $platformName for $mcVersion")
        projectVersion.set("${project.version}-${project.name}")
        loaders.add(project.name)
        gameVersions.addAll(prop("pub.game_version_supports").split(","))
    }
}
import dev.kikugie.semver.data.SemanticVersion

plugins {
    id("java-library")
    id("maven-publish")
    id("mmm.shadow-convention")
    id("me.modmuss50.mod-publish-plugin")
}

val (xVersion, loader) = name.split('-', limit = 2)
val modVersionString = "mod.version"()
val modVersion = SemanticVersion.parse(modVersionString)
val isSnapshot = modVersionString.uppercase().endsWith("-SNAPSHOT")

version = "${"mod.version"().removeSuffix("-SNAPSHOT")}+${sc(project).current.version}"

tasks {
    // Includes the license file in the built mod
    withType<Jar> {
        inputs.property("mod_id", "mod.id"())

        listOf("LICENSE", "NOTICE", "LICENSE.txt", "NOTICE.txt").forEach { p ->
            from(rootProject.file(p)) {
                rename { "${"mod.id"()}-${it}" }
            }
            from(rootProject.file(kotlin.io.path.Path(".custom", p))) {
                rename { "${"mod.id"()}-${it}" }
            }
        }
    }

    processResources {
        rootProject.file(".custom/assets/icon.png").let {
            if (it.exists()) from(it) { rename { "${"mod.id"()}_icon.png" } }
            else from(rootProject.file("assets/icon.png")) { rename { "${"mod.id"()}_icon.png" } }
        }

        fun MutableMap<String, String>.register(key: String, value: String) {
            inputs.property(key, value)
            set(key, value)
        }

        val props = buildMap {
            register("mod_id", "mod.id"())
            register("mod_name", "mod.name"())
            register("mod_version", "mod.version"().removeSuffix("-SNAPSHOT"))
            register("mod_license", "mod.license"())
            register("minecraft_compat", "mod.mc_compat"())
            if (isFabric) register("fabric_api", "deps.fabric_api"())
        }

        filesMatching(listOf("fabric.mod.json", "pack.mcmeta", "META-INF/neoforge.mods.toml")) {
            expand(props) { escapeBackslash = true }
        }

        filesMatching("*.mixins.json") { expand("java" to "JAVA_${requiredJava.majorVersion}") }
    }

    // https://github.com/projectlombok/lombok/issues/3852#issuecomment-3408877430
    if ((requiredJava.toClassVersion() - 44) >= 24) {
        withType<JavaCompile>().configureEach {
            options.forkOptions.jvmArgs?.add("--sun-misc-unsafe-memory-access=allow")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "${"mod.id"()}-$loader"
            groupId = "mod.group"()
            version = listOf(
                modVersion.components.joinToString("."),
                xVersion, modVersion.preRelease
            ).filter { it.isNotBlank() }.joinToString("-")

            from(components["java"])
        }
    }
}

publishMods {
    dryRun.set(providers.gradleProperty("publishMods.dryRun").map(String::toBoolean).orElse(true))

    type.set(if (isSnapshot) ALPHA else STABLE)
    file.set(modJar)
    modLoaders.add(loader)
    displayName.set("$modVersionString $loader $xVersion")
    changelog = if (isSnapshot) {
        buildString {
            appendLine("> ⚠️ This is a snapshot build and may be unstable.")
            rootProject.changelogSection("Unreleased")?.let {
                appendLine()
                append(it)
            }
        }
    } else {
        rootProject.changelogSection(modVersionString)
            ?: error("No CHANGELOG.md section found for version $modVersionString")
    }

    curseforge {
        projectId.set(sc(project).properties.getOrNull<String>("mod.curseProjectId"))
        accessToken.set(providers.gradleProperty("CURSEFORGE_TOKEN"))

        compatibleVersions.forEach { minecraftVersions.add(it) }
        optional("cloth-config")
        changelogType.set("markdown")
        client = true
    }
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(requiredJava.majorVersion.toInt())
    }

    val collectedLibs = rootProject.layout.buildDirectory.dir("libs/${"mod.version"()}")

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds mod jars and copies results to `build/libs/{mod version}/`"

        dependsOn("shadowJar")

        inputs.property("version", "mod.version"())
        from(modJar)
        into(collectedLibs)
    }

    named<Delete>("clean") {
        delete(collectedLibs)
    }
}

java {
    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava

    toolchain {
        vendor = JvmVendorSpec.ADOPTIUM
        languageVersion = JavaLanguageVersion.of(requiredJava.majorVersion)
    }
}

operator fun String.invoke(): String = project.ext[this] as? String ?: error("No property \"$this\"")
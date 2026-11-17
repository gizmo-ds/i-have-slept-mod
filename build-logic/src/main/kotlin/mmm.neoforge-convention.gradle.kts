@file:Suppress("SpellCheckingInspection")

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter

plugins {
    id("neoforge-mutex")
    id("mmm.platform-convention")
}

version = "${"mod.version"().removeSuffix("-SNAPSHOT")}-${sc(project).current.version}"
base.archivesName = "${"mod.id"()}-neoforge"

val localRuntime = configurations.create("localRuntime").apply {
    isCanBeResolved = true
    isCanBeConsumed = false
}
configurations.runtimeClasspath.get().extendsFrom(localRuntime)

val generateModMetadataFile = tasks.register("generateModMetadataFile") {
    group = "build"
    description = "Generates neoforge metadata file"

    val outputDir = layout.buildDirectory.dir("generated/mmm/resources")
    outputs.dir(outputDir)

    val inputFile = rootProject.layout.projectDirectory.file("src/main/templates/META-INF/neoforge.mods.toml")
    inputs.file(inputFile)
    inputs.property("mod.id", providers.provider { "mod.id"() })
    inputs.property("mod.name", providers.provider { "mod.name"() })
    inputs.property("mod.version", providers.provider { "mod.version"() })
    inputs.property("mod.description", providers.provider { "mod.description"() })
    inputs.property("mod.license", providers.provider { "mod.license"() })
    inputs.property(
        "mod.authors",
        providers.provider { sc(project).properties.rawOrNull("mod", "authors")?.toJson()?.toString() ?: "" })
    inputs.property(
        "mod.contact",
        providers.provider { sc(project).properties.rawOrNull("mod", "contact")?.toJson()?.toString() ?: "" })
    inputs.property(
        "mod.contributors",
        providers.provider { sc(project).properties.rawOrNull("mod", "contributors")?.toJson()?.toString() ?: "" })
    inputs.property("stonecutter.version", providers.provider { sc(project).current.version })

    doLast {
        val outputFile = outputDir.get().file("META-INF/neoforge.mods.toml").asFile
        outputFile.parentFile.mkdirs()

        val toml = Toml().read(inputFile.asFile).toMap()
        toml.let { root ->
            root["license"] = "mod.license"()
            val modmenu = root.getPath<MutableMap<String, Any>>("modproperties", "modmenu") ?: mutableMapOf()
            @Suppress("UNCHECKED_CAST")
            (root["mods"] as ArrayList<HashMap<String, Any>>)[0].let { mods ->
                mods["modId"] = "mod.id"()
                mods["version"] = "mod.version"().removeSuffix("-SNAPSHOT")
                mods["displayName"] = "mod.name"()
                mods["description"] = "mod.description"().trim()
                if (sc(project).current.parsed > "26") mods["iconFile"] = "${"mod.id"()}_icon.png"
                mods["logoFile"] = "${"mod.id"()}_icon.png"
                sc(project).properties.rawOrNull("mod", "authors")?.asList()
                    ?.let { v -> mods["authors"] = v.joinToString(", ") }
                sc(project).properties.rawOrNull("mod", "contact")?.asMap()?.forEach({
                    when (it.key) {
                        "homepage" -> mods["displayURL"] = it.value.toString()
                        "issues" -> root["issueTrackerURL"] = it.value.toString()
                        "sources" -> modmenu["sources"] = it.value.toString()
                    }
                })
                (root["dependencies"] as HashMap<String, Any?>).let {
                    it.remove("generated-at-build-time")?.let { v -> it["mod.id"()] = v }
                }
            }
            sc(project).properties.rawOrNull("mod", "contributors")?.asList()?.let {
                if (it.isNotEmpty()) modmenu["contributors"] = it.map { v -> v.toString() }
            }
            if (modmenu.isNotEmpty()) {
                if (!root.containsKey("modproperties")) root["modproperties"] = mutableMapOf<String, Any>()
                @Suppress("UNCHECKED_CAST")
                (root["modproperties"] as HashMap<String, Any>)["modmenu"] = modmenu
            }
        }
        outputFile.writer().use { writer ->
            TomlWriter().write(toml, writer)
        }
    }
}

tasks {
    processResources {
        dependsOn(generateModMetadataFile)
        from(generateModMetadataFile)

        exclude("fabric.mod.json", "*.ct", "*.classtweaker")
    }
}

inline fun <reified T> Map<String, *>.getPath(vararg path: String): T? {
    var current: Any? = this
    for (key in path) current = (current as? Map<*, *>)?.get(key) ?: return null
    return current as? T
}

operator fun String.invoke(): String = project.ext[this] as? String ?: error("No property \"$this\"")
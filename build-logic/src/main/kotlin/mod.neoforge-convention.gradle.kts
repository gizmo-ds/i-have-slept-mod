import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter

plugins {
    id("mod.platform-convention")
}

val generateModMetadata = tasks.register("generateModMetadata") {
    group = "build"
    description = "Generates META-INF/neoforge.mods.toml from src/main/templates"

    val template = rootProject.layout.projectDirectory.file("src/main/templates/META-INF/neoforge.mods.toml")
    val outputDir = layout.buildDirectory.dir("generated/mod-metadata")
    val values = mapOf(
        "id" to prop("mod.id"),
        "name" to prop("mod.name"),
        "version" to prop("mod.version"),
        "description" to prop("mod.description"),
        "license" to prop("mod.license"),
        "minecraft" to prop("mod.mc_compat"),
    )
    val authors = propList("mod", "authors")
    val contributors = propList("mod", "contributors")
    val contact = propMap("mod", "contact")

    inputs.file(template)
    inputs.properties(values)
    inputs.property("authors", authors)
    inputs.property("contributors", contributors)
    inputs.property("contact", contact)
    outputs.dir(outputDir)

    doLast {
        val id = values.getValue("id")
        val root = Toml().read(template.asFile).toMap()

        @Suppress("UNCHECKED_CAST")
        val properties = root.getOrPut("modproperties") { mutableMapOf<String, Any>() } as MutableMap<String, Any>
        @Suppress("UNCHECKED_CAST")
        val modmenu = properties.getOrPut("modmenu") { mutableMapOf<String, Any>() } as MutableMap<String, Any>

        root["license"] = values.getValue("license")
        @Suppress("UNCHECKED_CAST")
        (root["mods"] as List<MutableMap<String, Any>>)[0].let { mod ->
            mod["modId"] = id
            mod["version"] = values.getValue("version")
            mod["displayName"] = values.getValue("name")
            mod["description"] = values.getValue("description")
            mod["logoFile"] = "${id}_logo.png"
            mod["authors"] = authors.joinToString(", ")
            contact.forEach { (key, value) ->
                when (key) {
                    "homepage" -> mod["displayURL"] = value
                    "issues" -> root["issueTrackerURL"] = value
                    "sources" -> modmenu["sources"] = value
                }
            }
        }
        if (contributors.isNotEmpty()) modmenu["contributors"] = contributors

        @Suppress("UNCHECKED_CAST")
        val dependencies = root["dependencies"] as MutableMap<String, Any>
        @Suppress("UNCHECKED_CAST")
        val modDependencies = dependencies.remove("generated-at-build-time") as List<MutableMap<String, Any>>
        modDependencies.first { it["modId"] == "minecraft" }["versionRange"] = values.getValue("minecraft")
        dependencies[id] = modDependencies

        val output = outputDir.get().file("META-INF/neoforge.mods.toml").asFile
        output.parentFile.mkdirs()
        output.writer().use { TomlWriter().write(root, it) }
    }
}

tasks.processResources {
    from(generateModMetadata)
}

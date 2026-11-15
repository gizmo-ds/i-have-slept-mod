import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.node.ObjectNode

plugins {
    id("mod.platform-convention")
}

dependencies {
    "minecraft"("com.mojang:minecraft:${sc(project).current.version}")
    "modImplementation"("net.fabricmc:fabric-loader:${prop("deps.fabric_loader")}")
    "modImplementation"("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric_api")}")
}

val generateModMetadata = tasks.register("generateModMetadata") {
    group = "build"
    description = "Generates fabric.mod.json from src/main/templates"

    val template = rootProject.layout.projectDirectory.file("src/main/templates/fabric.mod.json")
    val outputDir = layout.buildDirectory.dir("generated/mod-metadata")
    val values = mapOf(
        "id" to prop("mod.id"),
        "name" to prop("mod.name"),
        "version" to prop("mod.version"),
        "description" to prop("mod.description"),
        "license" to prop("mod.license"),
        "minecraft" to prop("mod.mc_compat"),
        "java" to prop("java"),
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
        val mapper = ObjectMapper()
        val json = template.asFile.reader().use { mapper.readTree(it) } as ObjectNode
        json.put("id", values["id"])
        json.put("name", values["name"])
        json.put("version", values["version"])
        json.put("description", values["description"])
        json.put("license", values["license"])
        json.put("icon", "${values["id"]}_logo.png")
        json.set("authors", mapper.valueToTree(authors))
        json.set("contact", mapper.valueToTree(contact))
        if (contributors.isNotEmpty()) json.set("contributors", mapper.valueToTree(contributors))
        (json.get("depends") as ObjectNode).apply {
            put("minecraft", values["minecraft"])
            put("java", ">=${values["java"]}")
        }

        val output = outputDir.get().file("fabric.mod.json").asFile
        output.parentFile.mkdirs()
        output.writer().use { mapper.writerWithDefaultPrettyPrinter().writeValue(it, json) }
    }
}

tasks.processResources {
    from(generateModMetadata)
}

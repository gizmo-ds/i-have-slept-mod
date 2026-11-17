@file:Suppress("AvoidDuplicateDependencies")

import tools.jackson.databind.node.ObjectNode

plugins {
    id("mmm.platform-convention")
}

version = "${"mod.version"().removeSuffix("-SNAPSHOT")}-${sc(project).current.version}"
base.archivesName = "${"mod.id"()}-fabric"

val modCompileOnly = configurations.getByName("modCompileOnly")
val modLocalRuntime = configurations.getByName("modLocalRuntime")
val modImplementation = configurations.getByName("modImplementation")

repositories {
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${"deps.fabric_loader"()}")
    "minecraft"("com.mojang:minecraft:${sc(project).current.version}")
}

val generateModMetadataFile = tasks.register("generateModMetadataFile") {
    group = "build"
    description = "Generates fabric metadata file"

    val outputDir = layout.buildDirectory.dir("generated/mmm/resources")
    outputs.dir(outputDir)

    val inputFile = rootProject.layout.projectDirectory.file("src/main/templates/fabric.mod.json")
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
    inputs.property("modmenu", providers.provider { sc(project).constants["modmenu"]?.toString() ?: "" })

    doLast {
        val outputFile = outputDir.get().file("fabric.mod.json").asFile

        val mapper = tools.jackson.databind.ObjectMapper()
        val json = mapper.readTree(inputFile.asFile.reader()) as ObjectNode
        json.let {
            it.put("id", "mod.id"())
            it.put("name", "mod.name"())
            it.put("version", "mod.version"().removeSuffix("-SNAPSHOT"))
            it.put("description", "mod.description"().trim())
            it.put("license", "mod.license"())
            it.put("icon", "${"mod.id"()}_icon.png")
            sc(project).properties.rawOrNull("mod", "authors")?.asList()
                ?.let { v -> it.set("authors", mapper.readTree(v.toJson().toString())) }
            sc(project).properties.rawOrNull("mod", "contact")
                ?.let { v -> it.set("contact", mapper.readTree(v.toJson().toString())) }
            sc(project).properties.rawOrNull("mod", "contributors")
                ?.let { v ->
                    if (v.asList().isNotEmpty()) it.set(
                        "contributors",
                        mapper.readTree(v.toJson().toString())
                    )
                }
            sc(project).constants["modmenu"]?.let { v ->
                if (!v) (it.get("entrypoints") as ObjectNode).remove("modmenu")
            }
        }

        outputFile.parentFile.mkdirs()
        outputFile.writer().use {
            mapper.writerWithDefaultPrettyPrinter().writeValue(it, json)
        }
    }
}

tasks {
    processResources {
        dependsOn(generateModMetadataFile)
        from(generateModMetadataFile)

        exclude("META-INF/neoforge.mods.toml", "META-INF/accesstransformer.cfg")
    }
}

operator fun String.invoke(): String = project.ext[this] as? String ?: error("No property \"$this\"")
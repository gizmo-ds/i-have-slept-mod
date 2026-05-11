import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.node.ArrayNode
import tools.jackson.databind.node.ObjectNode
import java.io.File

data class GenerationContext(
    val input: File,
    val output: File
)

data class PlatformMeta(
    val input: String,
    val output: String,
    val generator: (GenerationContext) -> Unit
)

fun generateFabricMetadata(ctx: GenerationContext) {
    ctx.output.parentFile.mkdirs()

    val mapper = ObjectMapper()
    val json = mapper.readTree(ctx.input.reader()) as ObjectNode
    json.let {
        it.put("id", mod.id)
        it.put("name", mod.name)
        it.put("version", mod.version)
        it.put("description", mod.description)
        it.put("license", mod.license)
        it.put("icon", "${mod.id}_logo.png")
        it.set("authors", mapper.valueToTree<ArrayNode>(mod.authors))
        it.set("contact", mapper.valueToTree<ObjectNode>(mod.contact))
    }
    ctx.output.writer().use {
        mapper.writerWithDefaultPrettyPrinter().writeValue(it, json)
    }
}

fun generateNeoForgeMetadata(ctx: GenerationContext) {
    ctx.output.parentFile.mkdirs()

    val toml = Toml().read(ctx.input).toMap()
    toml.let { root ->
        root["license"] = mod.license
        @Suppress("UNCHECKED_CAST")
        (root["mods"] as ArrayList<HashMap<String, Any>>)[0].let { mods ->
            mods["modId"] = mod.id
            mods["version"] = mod.version
            mods["displayName"] = mod.name
            mods["description"] = mod.description
            mods["logoFile"] = "${mod.id}_logo.png"
            mods["authors"] = mod.authors.joinToString(", ")
            mod.contact.forEach { (key, value) ->
                when (key) {
                    "homepage" -> mods["displayURL"] = value
                    "issues" -> root["issueTrackerURL"] = value
                }
            }
            (root["dependencies"] as HashMap<String, Any?>).let {
                it.remove("generated-at-build-time")?.let { v -> it[mod.id] = v }
            }
        }
    }
    ctx.output.writer().use { writer ->
        TomlWriter().write(toml, writer)
    }
}
import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import tools.jackson.databind.JsonNode
import tools.jackson.databind.ObjectMapper

fun sc(project: Project): StonecutterBuildExtension =
    project.extensions.findByType(StonecutterBuildExtension::class.java)!!

/** Loader of the current Stonecutter node, e.g. `fabric` for `26.3-fabric`. */
val Project.loader: String get() = sc(this).current.project.substringAfter('-')

/** A flat Stonecutter property such as `mod.id`, resolved for the current node. */
fun Project.prop(key: String): String = extra[key] as? String ?: error("No property \"$key\"")

/** A structured Stonecutter property (list or map) as a JSON tree, or `null` when it is missing. */
fun Project.propTree(vararg path: String): JsonNode? =
    sc(this).properties.rawOrNull(*path)?.let { ObjectMapper().readTree(it.toJson().toString()) }

fun Project.propList(vararg path: String): List<String> =
    propTree(*path)?.values()?.map { it.asString() } ?: emptyList()

fun Project.propMap(vararg path: String): Map<String, String> =
    propTree(*path)?.properties()?.associate { (key, value) -> key to value.asString() } ?: emptyMap()

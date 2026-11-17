@file:Suppress("unused", "SpellCheckingInspection", "UnusedReceiverParameter")

import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.named

fun sc(project: Project) = project.extensions.findByType(StonecutterBuildExtension::class.java)!!

val Project.isFabric get() = sc(project).current.project.endsWith("-fabric")
val Project.requiredJava: JavaVersion
    get() = when {
        sc(this).current.parsed >= "26.1" -> JavaVersion.VERSION_25
        sc(this).current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
        sc(this).current.parsed >= "1.18" -> JavaVersion.VERSION_17
        sc(this).current.parsed >= "1.17" -> JavaVersion.VERSION_16
        else -> JavaVersion.VERSION_1_8
    }
val Project.compatibleVersions: List<String>
    get() = sc(this).properties.raw("mod.mc_releases").asList().map(Any::toString)
val Project.isLinux: Boolean get() = System.getProperty("os.name").lowercase().contains("linux")

@Suppress("UNCHECKED_CAST")
private fun TaskProvider<*>.archiveFile(): Provider<RegularFile> =
    flatMap { it.property("archiveFile") as Provider<RegularFile> }

val Project.modJar
    get() = when {
        !isFabric && sc(project).current.parsed < "1.20.2" ->
            project.providers.provider { project.tasks.named<Jar>("reobfJar") }.flatMap { it.flatMap { jar -> jar.archiveFile } }
        !isFabric || sc(project).current.parsed >= "26" -> project.tasks.named<Jar>("jar").archiveFile()
        else -> project.tasks.named("remapJar").archiveFile()
    }

fun RepositoryHandler.strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
    forRepository { maven(url) { name = alias } }
    filter { groups.forEach(::includeGroup) }
}

/**
 * Extracts the body of the `CHANGELOG.md` section that belongs to the given mod version.
 *
 * The section is the part under the heading that mentions [version] (e.g. `# 0.1.0 - 2026-11-07`),
 * up until the next heading of the same or a higher level.
 * Returns `null` when the changelog file or the matching section does not exist.
 */
fun Project.changelogSection(version: String): String? {
    val file = rootProject.file("CHANGELOG.md")
    if (!file.exists()) return null

    val lines = file.readLines()
    val target = version.removeSuffix("-SNAPSHOT").removePrefix("v")
    val heading = Regex("^(#{1,6})\\s+(.+?)\\s*$")
    val versionToken = Regex("\\d+(?:\\.\\d+)*(?:[-+][0-9A-Za-z.\\-]+)?")

    var start = -1
    var level = 0
    for ((index, line) in lines.withIndex()) {
        val match = heading.find(line) ?: continue
        val title = match.groupValues[2].trim().trim('[', ']', '`')
        val titleVersion = versionToken.find(title)?.value?.removePrefix("v")
        if (titleVersion == target || title.equals(target, ignoreCase = true)) {
            start = index
            level = match.groupValues[1].length
            break
        }
    }
    if (start == -1) return null

    var end = lines.size
    for (index in start + 1 until lines.size) {
        val match = heading.find(lines[index]) ?: continue
        if (match.groupValues[1].length <= level) {
            end = index
            break
        }
    }

    return lines.subList(start + 1, end).joinToString("\n").trim().takeIf { it.isNotEmpty() }
}

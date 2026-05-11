val mod = ModInfo()

data class ModInfo(
    val id: String = "i_have_slept",
    val group: String = "dev.aika.i_have_slept",
    val version: String = "1.1.2",
    val name: String = "I have slept",
    val authors: List<String> = listOf(
        "Gizmo"
    ),
    // https://spdx.org/licenses/
    var license: String = "MIT",
    val description: String = """
        This mod resets all players' sleep counters after skipping the night in multiplayer mode, ensuring no phantoms spawn for those who didn't sleep.
    """.trimIndent(),

    val contact: Map<String, String> = mapOf(
        "homepage" to "https://github.com/gizmo-ds/i-have-slept-mod",
        "sources" to "https://github.com/gizmo-ds/i-have-slept-mod",
        "issues" to "https://github.com/gizmo-ds/i-have-slept-mod/issues"
    ),

    val javaVersion: Int = 21
)
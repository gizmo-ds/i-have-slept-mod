plugins {
    id("dev.kikugie.stonecutter")
    id("net.neoforged.moddev") version libs.versions.moddev.get() apply false
    id("net.neoforged.moddev.legacyforge") version libs.versions.moddev.get() apply false
}

stonecutter active file("versions/current")

allprojects {
    repositories {
        mavenCentral()

        exclusiveContent {
            forRepository { maven("https://api.modrinth.com/maven") { name = "Modrinth" } }
            filter { includeGroup("maven.modrinth") }
        }
        exclusiveContent {
            forRepository { maven("https://maven.parchmentmc.org") { name = "ParchmentMC" } }
            filter { includeGroup("org.parchmentmc.data") }
        }
    }
}

stonecutter parameters {
    val (version, loader) = current.project.split('-', limit = 2)

    properties.tags(version, loader)
    constants.match(loader, "fabric", "forge", "neoforge")

    replacements {
        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
            replace(
                "import net.minecraft.world.level.GameRules;",
                "import net.minecraft.world.level.gamerules.GameRules;"
            )
            replace("getGameRules().getBoolean(", "getGameRules().get(")
        }
    }
}
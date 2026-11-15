plugins {
    id("dev.kikugie.stonecutter")
    id("net.neoforged.moddev") version libs.versions.moddev.get() apply false
}

stonecutter active file("versions/current")

allprojects {
    repositories {
        mavenCentral()

        exclusiveContent {
            forRepository { maven("https://api.modrinth.com/maven") { name = "Modrinth" } }
            filter { includeGroup("maven.modrinth") }
        }
    }
}

stonecutter parameters {
    val (version, loader) = current.project.split('-', limit = 2)

    properties.tags(version, loader)
    constants.match(loader, "fabric", "neoforge")
}

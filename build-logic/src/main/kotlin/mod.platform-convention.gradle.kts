plugins {
    id("java-library")
}

val modId = prop("mod.id")
val modVersion = prop("mod.version")
val javaVersion = prop("java").toInt()

group = prop("mod.group")
version = modVersion
base.archivesName = "$modId-$loader-${sc(project).current.version}"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(javaVersion)
    withSourcesJar()
}

// Each loader only compiles and ships its own platform package.
val otherLoaders = listOf("fabric", "neoforge") - loader
sourceSets.main {
    java { otherLoaders.forEach { exclude("dev/aika/i_have_slept/$it/**") } }
}

/** A file from the repository root, or its override from assets/custom/ when present. */
fun customizable(path: String, override: String = path): File =
    rootProject.file("assets/custom/$override").takeIf(File::exists) ?: rootProject.file(path)

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release = javaVersion
    }

    withType<Jar>().configureEach {
        listOf("LICENSE", "NOTICE").forEach { name -> from(customizable(name)) }
        from(rootProject.file("third-party-licenses")) {
            into("third-party-licenses")
            exclude("**/.gitkeep")
        }
    }

    processResources {
        from(customizable("assets/logo.png", "logo.png")) { rename { "${modId}_logo.png" } }
    }

    val collectedLibs = rootProject.layout.buildDirectory.dir("libs/$modVersion")

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds the mod jar and copies it to `build/libs/{mod version}/` of the root project"

        from(named<Jar>("jar").flatMap { it.archiveFile })
        into(collectedLibs)
    }
}

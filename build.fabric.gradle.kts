plugins {
    id("dev.kikugie.loom-back-compat")
    id("mod.fabric-convention")
}

val serverRunDir = rootProject.file("runs/server/${sc(project).current.project}")

repositories {
    exclusiveContent {
        forRepository { maven("https://maven.terraformersmc.com/") { name = "Terraformers" } }
        filter { includeGroup("com.terraformersmc") }
    }
}

dependencies {
    findProperty("deps.modmenu")?.let { "modLocalRuntime"("com.terraformersmc:modmenu:$it") }
}

loom {
    runConfigs.maybeCreate("server").apply {
        runDirectory = serverRunDir
    }
    runConfigs.all {
        preferGradleTask = true
        generateRunConfig = true
    }

    mods { create(prop("mod.id")) { sourceSet(sourceSets.main.get()) } }
}

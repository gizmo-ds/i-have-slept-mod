plugins {
    id("net.neoforged.moddev")
    id("mod.neoforge-convention")
}

val serverRunDir = rootProject.file("runs/server/${sc(project).current.project}")

val localRuntime = configurations.create("localRuntime")
configurations.runtimeClasspath { extendsFrom(localRuntime) }

dependencies {
    findProperty("deps.better_modlist")?.let { localRuntime("maven.modrinth:better-modlist:$it") }
}

neoForge {
    version = prop("deps.neoforge")

    mods { register(prop("mod.id")) { sourceSet(sourceSets.main.get()) } }

    runs {
        register("server") {
            server()
            gameDirectory = serverRunDir
            programArguments.add("--nogui")
        }
    }
}

tasks.named("createMinecraftArtifacts") {
    dependsOn("stonecutterGenerate")
}

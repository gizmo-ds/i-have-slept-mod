plugins {
    id("net.neoforged.moddev")
    id("mmm.neoforge-convention")
}

publishMods {
    curseforge {
        server.set(true)
    }
}

neoForge {
    rootProject.file("src/main/resources/META-INF/accesstransformer.cfg").takeIf(File::exists)?.let {
        accessTransformers.from(sc.process(it, "build/processed.cfg"))
    }

    version = "deps.neoforge"()

    if (sc.current.parsed < "26") sc.properties.getOrNull<String>("parchment.mappings_version")?.let {
        parchment {
            minecraftVersion =
                sc.properties.getOrNull<String>("parchment.minecraft_version") ?: sc.current.parsed.toString()
            mappingsVersion = it
        }
    }

    mods {
        register("mod.id"()) { sourceSet(sourceSets.main.get()) }
    }

    runs {
        register("client") {
            client()
            gameDirectory = rootProject.file("runs/client")
        }
        register("server") {
            server()
            gameDirectory = rootProject.file("runs/server/${sc.current.version}")
            programArguments.add("--nogui")
        }
        configureEach {
            // https://github.com/neoforged/ModDevGradle/issues/353
            disableIdeRun()
            loggingConfigFile = rootProject.file("build-logic/src/main/resources/log4j2.xml")
        }
    }
}

tasks.named("createMinecraftArtifacts") {
    dependsOn("stonecutterGenerate")
}

operator fun String.invoke(): String = project.ext[this] as? String ?: error("No property \"$this\"")
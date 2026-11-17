plugins {
    id("net.neoforged.moddev.legacyforge")
    id("mmm.neoforge-convention")
}

publishMods {
    curseforge {
        server.set(true)
    }
}

legacyForge {
    enable { forgeVersion = "deps.forge"() }
    parchment {
        minecraftVersion = "parchment.minecraft_version"()
        mappingsVersion = "parchment.mappings_version"()
    }
    mods {
        register("mod.id"()) { sourceSet(sourceSets.main.get()) }
    }
    runs {
        register("client") {
            client()
            gameDirectory = rootProject.file("runs/client/${sc.current.version}")
        }
        register("server") {
            server()
            gameDirectory = rootProject.file("runs/server/${sc.current.version}")
            programArguments.add("--nogui")
        }
        configureEach {
             disableIdeRun()
        }
    }
}

dependencies {
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

mixin {
    add(sourceSets.main.get(), "i_have_slept.refmap.json")
    config("i_have_slept.mixins.json")
}

tasks.jar {
    manifest.attributes("MixinConfigs" to "i_have_slept.mixins.json")
}

// Use the SRG jar for consumers as well as publishing and collection tasks.
configurations.apiElements {
    outgoing.artifacts.clear()
    outgoing.artifact(tasks.named("reobfJar"))
}
configurations.runtimeElements {
    outgoing.artifacts.clear()
    outgoing.artifact(tasks.named("reobfJar"))
}

tasks.named("createMinecraftArtifacts") { dependsOn("stonecutterGenerate") }

operator fun String.invoke(): String = project.ext[this] as? String ?: error("No property \"$this\"")
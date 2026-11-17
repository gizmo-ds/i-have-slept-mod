plugins {
    id("dev.kikugie.loom-back-compat")
    id("mmm.fabric-convention")
}

repositories {
    strictMaven("https://maven.terraformersmc.com/", "Terraformers", "com.terraformersmc")
}

dependencies {
    val fabricApiModules = mutableSetOf<String>()
    fabricApiModules.add("fabric-registry-sync-v0")

    // Mod Menu
    findProperty("deps.modmenu")?.let { version ->
        val modmenu = "com.terraformersmc:modmenu:$version"
        modCompileOnly(modmenu)
        modLocalRuntime(modmenu) {
            exclude(group = "net.fabricmc", module = "fabric-loader")
            exclude(group = "net.fabricmc.fabric-api")
        }
        listOf(
            "fabric-lifecycle-events-v1",
            if (sc.current.parsed >= "1.21.9") "fabric-resource-loader-v1" else "fabric-resource-loader-v0",
            "fabric-screen-api-v1",
            if (sc.current.parsed >= "26.1") "fabric-key-mapping-api-v1" else "fabric-key-binding-api-v1"
        ).forEach { fabricApiModules.add(it) }
    }

    val fabricApiVersion: String = sc.properties["deps.fabric_api"]
    fabricApiModules.forEach { module -> modImplementation(fabricApi.module(module, fabricApiVersion)) }
}

dependencies {
    if (sc.current.parsed < "26") {
        @Suppress("UnstableApiUsage")
        mappings(loom.layered {
            officialMojangMappings()
            findProperty("parchment.mappings_version")?.let { version ->
                val mcVersion = findProperty("parchment.minecraft_version") ?: sc.current.parsed
                parchment("org.parchmentmc.data:parchment-$mcVersion:$version@zip")
            }
        })
    }
}

loom {
    rootProject.file("src/main/resources/${"mod.id"()}.ct").takeIf(File::exists)?.let {
        accessWidenerPath = sc.process(it, "build/processed.ct")
    }

    decompilerOptions.named("vineflower") {
        options.put("mark-corresponding-synthetics", "1") // Adds names to lambdas - useful for mixins
    }

    runConfigs.maybeCreate("client").apply {
        runDirectory = rootProject.file("runs/client")
        programArguments.addAll("--username", "Dev")
    }
    runConfigs.maybeCreate("server").apply {
        runDirectory = rootProject.file("runs/server/${sc.current.version}")
    }
    runConfigs.all {
        preferGradleTask = true
        generateRunConfig = true
        jvmArguments.add("-Dmixin.debug.export=true") // Exports transformed classes for debugging
    }

    mods { create("mod.id"()) { sourceSet(sourceSets.main.get()) } }
}

operator fun String.invoke(): String = project.ext[this] as? String ?: error("No property \"$this\"")

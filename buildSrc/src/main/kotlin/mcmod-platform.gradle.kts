plugins {
    id("mcmod-base")
    id("com.gradleup.shadow")
}

architectury {
    platformSetupLoomIde()
    loader(project.name)
}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()
val platformName = loom.platform.get().displayName()

val common: Configuration by configurations.creating
val shadowBundle: Configuration by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}
configurations {
    compileOnly.configure { extendsFrom(common) }
    runtimeOnly.configure { extendsFrom(common) }

    configurations.getByName("development$platformName").extendsFrom(common)
}

dependencies {
    common(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    shadowBundle(project(path = ":common", configuration = "transformProduction$platformName"))
}

tasks {
    val copyLicense: CopySpec by project(":common").extra

    val generatePlatformMetadata = tasks.register("generatePlatformMetadata") {
        description = "Generates platform-specific mod metadata (Fabric / Forge / NeoForge)"

        val platforms = mapOf(
            "fabric" to PlatformMeta(
                input = "src/main/resources/fabric.mod.json",
                output = "fabric.mod.json",
                generator = { ctx -> generateFabricMetadata(ctx) }
            ),
            "neoforge" to PlatformMeta(
                input = "src/main/resources/META-INF/neoforge.mods.toml",
                output = "META-INF/neoforge.mods.toml",
                generator = { ctx -> generateNeoForgeMetadata(ctx) }
            ),
            "forge" to PlatformMeta(
                input = "src/main/resources/META-INF/mods.toml",
                output = "META-INF/mods.toml",
                generator = { ctx -> generateNeoForgeMetadata(ctx) }
            )
        )
        val outputDir = layout.buildDirectory.dir("generated/platform-resources")
        outputs.dir(outputDir)
        platforms[project.name]?.let { meta -> inputs.file(project.file(meta.input)) }
        val gen = outputDir.map { dir ->
            val meta = platforms[project.name] ?: return@map
            meta.generator(
                GenerationContext(
                    project.file(meta.input),
                    dir.file(meta.output).asFile,
                )
            )
        }
        doLast { gen.get() }
    }

    sourceSets.named("main") { resources.srcDir(generatePlatformMetadata) }

    processResources {
        dependsOn(generatePlatformMetadata)

        val expandProps = mapOf(
            "version" to mod.version,
            "group" to mod.group,
            "mod_id" to mod.id,
            "mod_name" to mod.name,
            "license" to mod.license,
        )

        filesMatching(listOf("pack.mcmeta", "*.mixins.json")) {
            expand(expandProps.mapValues { (_, value) -> value.replace("\n", "\\n") })
        }

        inputs.properties(expandProps)

        from(rootProject.file("assets/logo.png")) { rename { "${mod.id}_logo.png" } }
        from(rootProject.file("assets/custom/logo.png")) { rename { "${mod.id}_logo.png" } }
    }

    withType<Jar> { duplicatesStrategy = DuplicatesStrategy.INCLUDE }

    shadowJar {
        exclude("META-INF/maven/**/*", "META-INF/versions/**/*")

        archiveClassifier.set("dev-shadow")
        configurations = listOf(shadowBundle)

        mergeServiceFiles()
    }

    remapJar {
        inputFile.set(shadowJar.flatMap { it.archiveFile })
        with(copyLicense)
        dependsOn(shadowJar)
    }
}
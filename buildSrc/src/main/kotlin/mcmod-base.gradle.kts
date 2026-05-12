plugins {
    id("java-library")
    id("architectury-plugin")
    id("dev.architectury.loom-no-remap")
}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

base {
    archivesName.set("${mod.id}-${project.name}-${libs.versions.minecraft.get()}")
}

dependencies {
    minecraft("com.mojang:minecraft:${libs.versions.minecraft.get()}")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(mod.javaVersion))

    withSourcesJar()
}

val copyLicense = copySpec {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    licenseFile("LICENSE")
    licenseFile("NOTICE")
    from(project.file("third-party-licenses")) {
        into("third-party-licenses")
        exclude("**/.gitkeep")
    }
}

fun CopySpec.licenseFile(name: String) {
    from(rootProject.file(name)) { into("") }
    from(rootProject.file("assets/custom/$name")) { rename { name }; into("") }
}

project(":common").extra["copyLicense"] = copyLicense

tasks {
    named<Jar>("sourcesJar") {
        with(copyLicense)
    }

    processResources { duplicatesStrategy = DuplicatesStrategy.INCLUDE }

    named("clean") { doLast { delete("logs") } }
}
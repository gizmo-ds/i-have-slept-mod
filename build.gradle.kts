plugins {
    java
    id("architectury-plugin")
}

architectury { minecraft = libs.versions.minecraft.get() }

allprojects {
    group = mod.group
    version = mod.version
}

subprojects {
    plugins.apply("dev.architectury.loom")

    val libs = rootProject.libs

    repositories {
        maven("https://api.modrinth.com/maven") {
            content { includeGroup("maven.modrinth") }
        }
    }

    dependencies {
    }
}
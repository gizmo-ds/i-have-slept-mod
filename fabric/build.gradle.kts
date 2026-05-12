plugins {
    id("mcmod-platform")
}

repositories {
    maven("https://maven.terraformersmc.com/") { name = "Terraformers" }
}

dependencies {
    modImplementation(libs.fabric.loader)

    modRuntimeOnly(libs.fabric.api)
    modRuntimeOnly(libs.fabric.modmenu)
}
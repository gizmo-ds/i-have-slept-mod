plugins {
    id("mcmod-platform")
}

repositories {
    maven("https://maven.terraformersmc.com/") { name = "Terraformers" }  // ModMenu
}

dependencies {
    modImplementation(libs.fabric.loader)

    modRuntimeOnly(libs.fabric.api)
    modRuntimeOnly(libs.fabric.modmenu)
}
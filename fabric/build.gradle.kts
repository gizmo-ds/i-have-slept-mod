plugins {
    id("mcmod-platform")
}

repositories {
    maven("https://maven.terraformersmc.com/") { name = "Terraformers" }  // ModMenu
}

dependencies {
    implementation(libs.fabric.loader)

    runtimeOnly(libs.fabric.api)
    runtimeOnly(libs.fabric.modmenu)
}
plugins {
    id("mcmod-platform")
}

repositories {
    maven("https://maven.neoforged.net/releases") { name = "NeoForged" }
}

dependencies {
    @Suppress("USELESS_IS_CHECK")
    if (libs.neoforge is Provider<*>) neoForge(libs.create("neoforge")) else neoForge(libs.neoforge)

    runtimeOnly(libs.neoforge.bettermodlist)
}
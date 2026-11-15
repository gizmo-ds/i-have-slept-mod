import java.util.Properties

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
}

val rootProperties = Properties().apply { file("../gradle.properties").inputStream().use(::load) }

dependencies {
    implementation("tools.jackson.core:jackson-core:3.2.4")
    implementation("tools.jackson.core:jackson-databind:3.2.4")
    implementation("io.hotmoka:toml4j:0.7.3")

    val stonecutter = "dev.kikugie.stonecutter"
    implementation("$stonecutter:$stonecutter.gradle.plugin:${rootProperties.getProperty("stonecutter")}")
}
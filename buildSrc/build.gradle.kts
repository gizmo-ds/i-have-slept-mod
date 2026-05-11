plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.fabricmc.net/")
    maven("https://maven.architectury.dev/")
    maven("https://files.minecraftforge.net/maven/")
}

dependencies {
    // https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation("tools.jackson.core:jackson-core:3.1.2")
    implementation("tools.jackson.core:jackson-databind:3.1.2")
    implementation("io.hotmoka:toml4j:0.7.3")

    implementation(libs.shadow)
    implementation(libs.architectury.plugin)
    implementation(libs.architectury.loom)
}
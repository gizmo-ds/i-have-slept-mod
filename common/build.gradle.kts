plugins {
    id("mcmod-base")
}

architectury {
    common(rootProject.childProjects.keys.filter { listOf("fabric", "forge", "neoforge").contains(it) })
}

dependencies {
    implementation(libs.fabric.loader)
}
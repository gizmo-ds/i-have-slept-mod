architectury {
    common(rootProject.property("enabled_platforms").toString().split(','))
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric_loader_version")}")
}

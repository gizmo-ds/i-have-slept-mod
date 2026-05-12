plugins {
    id("mcmod-platform")
}

loom.forge {
    mixinConfig("i_have_slept.mixins.json")
}

dependencies {
    @Suppress("USELESS_IS_CHECK")
    if (libs.forge is Provider<*>) forge(libs.create("forge")) else forge(libs.forge)
}
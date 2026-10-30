plugins {
    id("ddcmp.library.compose")
    alias(libs.plugins.serialization)
}

kotlin {
    explicitApi()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.backhandler)
        }
    }
}
plugins {
    id("ddcmp.library.compose")
    alias(libs.plugins.serialization)
}

kotlin {
    explicitApi()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.android.biometric)
        }
        commonMain.dependencies {
            implementation(libs.serialization.json)
        }
    }
}
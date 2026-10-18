plugins {
    id("ddcmp.library.compose")
    alias(libs.plugins.serialization)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.android.biometric)
        }
        commonMain.dependencies {
            implementation(libs.serialization.json)
        }
    }
}
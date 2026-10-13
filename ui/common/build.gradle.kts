plugins {
    id("ddcmp.library.compose")
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.android.biometric)
        }
        commonMain.dependencies {
            implementation(project(":domain"))
        }
    }
}
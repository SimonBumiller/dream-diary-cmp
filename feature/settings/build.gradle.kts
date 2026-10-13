plugins {
    id("ddcmp.library.compose")
    alias(libs.plugins.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":domain"))
            implementation(project(":ui:common"))
        }
    }
}
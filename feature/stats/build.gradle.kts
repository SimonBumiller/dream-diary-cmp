plugins {
    id("ddcmp.library.compose")
    alias(libs.plugins.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.backhandler)
            implementation(project(":ui:diagram"))
        }
    }
}
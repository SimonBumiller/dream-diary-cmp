plugins {
    id("ddcmp.library.compose")
    alias(libs.plugins.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.backhandler)
            implementation(libs.compose.navigation)
            implementation(project(":feature:stats"))
            implementation(project(":ui:diagram"))
        }
    }
}
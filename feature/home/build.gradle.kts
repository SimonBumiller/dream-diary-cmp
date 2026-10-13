plugins {
    id("ddcmp.library.compose")
    alias(libs.plugins.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.backhandler)
            implementation(libs.compose.navigation)
            implementation(project(":domain"))
            implementation(project(":feature:stats"))
            implementation(project(":ui:common"))
            implementation(project(":ui:diagram"))
        }
    }
}
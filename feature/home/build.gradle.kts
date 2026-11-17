plugins {
    id("ddcmp.library.compose")
    alias(libs.plugins.serialization)
}

kotlin {
    explicitApi()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.backhandler)
            implementation(libs.compose.navigation)
            implementation(project(":ui:diagram"))

            implementation(project(":feature:stats"))
            implementation(project(":feature:person"))
            implementation(project(":feature:dream"))
            implementation(project(":feature:emotion"))
            implementation(project(":feature:tag"))
        }
    }
}
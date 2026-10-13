plugins {
    id("ddcmp.library.compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":domain"))
            implementation(project(":ui:common"))
        }
    }
}
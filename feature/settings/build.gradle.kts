plugins {
    id("ddcmp.library")
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization)
    alias(libs.plugins.compose.hotreload)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.android.activity.compose)
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.material)
            implementation(libs.compose.ui)
            implementation(libs.compose.resources)
            implementation(libs.android.compose.viewmodel)
            implementation(libs.android.compose.lifecycle)
            implementation(libs.material.icons)
            implementation(libs.compose.navigation)
            implementation(libs.filekt.dialogs)
            implementation(libs.filekt.dialogs.compose)
            implementation(project(":domain"))
            implementation(project(":ui:common"))
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlin.coroutines.swing)
        }
    }
}
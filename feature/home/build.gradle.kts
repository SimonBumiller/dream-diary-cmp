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
            //implementation(libs.backhandler)
            implementation(libs.compose.backhandler)
            implementation(libs.material.windowsizeclass)
            implementation(libs.compose.navigation)
            implementation(project(":domain"))
            implementation(project(":feature:stats"))
            implementation(project(":ui:common"))
            implementation(project(":ui:diagram"))
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlin.coroutines.swing)
        }
    }
}
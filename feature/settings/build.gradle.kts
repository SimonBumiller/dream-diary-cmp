import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.compose.hotreload)
}

kotlin {
    android {
        namespace = "me.bumiller.ddcmp.feature.settings"
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        androidResources {
            enable = true
        }
    }

    jvm()

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

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
            implementation(libs.koin)
            implementation(libs.material.icons)
            implementation(libs.compose.navigation)
            implementation(libs.filekt.core)
            implementation(libs.filekt.dialogs)
            implementation(libs.filekt.dialogs.compose)
            implementation(project(":domain"))
            implementation(project(":ui:common"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlin.coroutines.swing)
        }
    }
}
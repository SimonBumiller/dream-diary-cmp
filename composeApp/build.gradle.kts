import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.hotreload)
    alias(libs.plugins.serialization)
}

kotlin {
    android {
        namespace = "me.bumiller.ddcmp.compose_app"
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        androidResources {
            enable = true
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm()
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.android.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.workmanager)
            implementation(libs.filekt.core)
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
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.compose.navigation)
            implementation(libs.material.windowsizeclass)
            implementation(libs.filekt.core)
            implementation(project(":core:database"))
            implementation(project(":core:datastore"))
            implementation(project(":core:io"))
            implementation(project(":core:work"))
            implementation(project(":domain"))
            implementation(project(":feature:home"))
            implementation(project(":feature:settings"))
            implementation(project(":feature:io"))
            implementation(project(":feature:stats"))
            implementation(project(":feature:onboarding"))
            implementation(project(":ui:common"))
            implementation(project(":ui:diagram"))
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

//
// App Icon On Desktop does not work.
// See https://youtrack.jetbrains.com/projects/CMP/issues/CMP-9837/packageDeb-task-fails-to-include-custom-.desktop-files?utm_source=chatgpt.com
//

private val iconFileIco = project.file("./src/jvmMain/resources/jvm_icon.ico")
private val iconFilePng = project.file("./src/jvmMain/resources/jvm_icon.png")

compose.desktop {
    application {
        mainClass = "me.bumiller.ddcmp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

            packageName = "me.bumiller.ddcmp"
            packageVersion = "1.0.0"

            windows {
                iconFile.set(iconFileIco)
            }

            linux {
                iconFile.set(iconFilePng)
            }
        }
    }
}

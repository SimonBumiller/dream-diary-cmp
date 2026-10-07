import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.serialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
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
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.filekt.core)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.compose.navigation)
            implementation(libs.compose.material3.windowsizeclass)
            implementation(project(":core:database"))
            implementation(project(":core:datastore"))
            implementation(project(":core:io"))
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
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

android {
    namespace = "me.bumiller.ddcmp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "me.bumiller.ddcmp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        val keystorePropertiesFile = rootProject.file("keystore.properties")
        val keystoreProperties = Properties()

        if (keystorePropertiesFile.exists()) {
            keystoreProperties.load(keystorePropertiesFile.inputStream())
        }

        create("release") {
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
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

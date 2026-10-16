import com.android.build.api.dsl.ApplicationExtension
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.sentry)
}

extensions.configure<ApplicationExtension> {
    namespace = "me.bumiller.ddcmp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "me.bumiller.ddcmp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.app.version.code.get().toInt()
        versionName = libs.versions.app.version.name.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    signingConfigs {
        val keystorePropertiesFile = rootProject.file("build.properties")
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
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false

            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }

        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

sentry {
    includeSourceContext = true

    org = "simon-bumiller"
    projectName = "kotlin"

    val buildPropertiesFile = rootProject.file("build.properties")
    val buildProperties = Properties()

    if (buildPropertiesFile.exists()) {
        buildProperties.load(buildPropertiesFile.inputStream())
    }

    authToken = buildProperties.getProperty("sentryAuthToken")
}

dependencies {
    implementation(project(":composeApp"))
    implementation(libs.android.activity.compose)
    implementation(libs.material)
    implementation(libs.material.windowsizeclass)
    implementation(libs.koin)
    implementation(libs.koin.android)
    implementation(libs.workmanager)
    implementation(libs.android.splashscreen)

    implementation(project(":core:work"))
}
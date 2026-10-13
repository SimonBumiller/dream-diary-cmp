import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("library-plugin") {
            id = "ddcmp.library"
            implementationClass = "LibraryPlugin"
        }
        register("compose-library-plugin") {
            id = "ddcmp.library.compose"
            implementationClass = "ComposeLibraryPlugin"
        }
    }
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.android.gradle.plugin)
}
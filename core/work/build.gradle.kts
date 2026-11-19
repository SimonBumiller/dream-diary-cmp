plugins {
    id("ddcmp.library")
    alias(libs.plugins.serialization)
}

kotlin {
    explicitApi()

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.serialization.json)

                implementation(project(":domain"))
            }
        }
        androidMain {
            dependencies {
                implementation(libs.workmanager)
            }
        }
        iosMain {
            dependencies {
                implementation(libs.workmanager)
            }
        }
    }

}
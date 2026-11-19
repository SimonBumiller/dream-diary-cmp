plugins {
    id("ddcmp.library")
}

kotlin {
    explicitApi()

    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.androidx.documentfile)
                implementation(libs.sentry)
            }
        }
    }
}
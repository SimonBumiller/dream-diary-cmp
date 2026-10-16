plugins {
    id("ddcmp.library")
}

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.androidx.documentfile)
                implementation(libs.sentry)
            }
        }
    }
}
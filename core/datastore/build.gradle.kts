plugins {
    id("ddcmp.library")
}

kotlin {
    explicitApi()

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.android.datastore)
                implementation(libs.androidx.datastore.preferences)
            }
        }
    }

}
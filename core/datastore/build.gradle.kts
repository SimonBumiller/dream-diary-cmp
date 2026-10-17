plugins {
    id("ddcmp.library")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.android.datastore)
                implementation(libs.androidx.datastore.preferences)
            }
        }
    }

}
plugins {
    id("ddcmp.library.common")
}

kotlin {
    explicitApi()
}

kotlin.sourceSets.androidMain.get().dependencies {
    implementation(libs.androidx.documentfile)
    implementation(libs.sentry)
}
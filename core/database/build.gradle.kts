plugins {
    id("ddcmp.library")
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.room)
}

dependencies {
    add("ksp", libs.android.room.compiler)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":domain"))

                implementation(libs.android.room.runtime)
                implementation(libs.android.sqlite.driver)
            }
        }
    }

}

room {
    schemaDirectory("$projectDir/schemas")
}
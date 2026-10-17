plugins {
    id("ddcmp.library")
    alias(libs.plugins.serialization)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.serialization.json)
                implementation(libs.serialization.json.io)
                implementation(libs.serialization.yaml)
                implementation(libs.serialization.xml)
                implementation(libs.serialization.xml.core)
                implementation(libs.serialization.xml.io)

                implementation(libs.zip)
                implementation(libs.zip.kotlinx)
            }
        }
        jvmTest {
            dependencies {
                implementation(libs.mockk)
            }
        }
    }

}
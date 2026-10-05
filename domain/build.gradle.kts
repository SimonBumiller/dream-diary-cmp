plugins {
    alias(libs.plugins.kotlinMultiplatform)
}
kotlin {
    jvm {
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
        }
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.coroutines)
                implementation(libs.koin.core)
                implementation(libs.kotlin.datetime)
                implementation(libs.filekt.core)
            }
        }
    }
}
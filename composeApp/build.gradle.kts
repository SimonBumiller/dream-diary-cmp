import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("ddcmp.library.compose")
    alias(libs.plugins.serialization)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.workmanager)
        }
        commonMain.dependencies {
            implementation(project(":core:database"))
            implementation(project(":core:datastore"))
            implementation(project(":core:io"))
            implementation(project(":core:work"))
            implementation(project(":domain"))
            implementation(project(":feature:home"))
            implementation(project(":feature:settings"))
            implementation(project(":feature:io"))
            implementation(project(":feature:stats"))
            implementation(project(":feature:onboarding"))
            implementation(project(":ui:common"))
            implementation(project(":ui:diagram"))
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

//
// App Icon On Desktop does not work.
// See https://youtrack.jetbrains.com/projects/CMP/issues/CMP-9837/packageDeb-task-fails-to-include-custom-.desktop-files?utm_source=chatgpt.com
//

private val iconFileIco = project.file("./src/jvmMain/resources/jvm_icon.ico")
private val iconFilePng = project.file("./src/jvmMain/resources/jvm_icon.png")

compose.desktop {
    application {
        mainClass = "me.bumiller.ddcmp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

            packageName = "me.bumiller.ddcmp"
            packageVersion = libs.versions.app.version.name.get()

            windows {
                iconFile.set(iconFileIco)
            }

            linux {
                iconFile.set(iconFilePng)
            }
        }
    }
}

import me.bumiller.ddcmp.build.DdcmpConventionPlugin
import me.bumiller.ddcmp.build.android
import me.bumiller.ddcmp.build.findIntVersion
import me.bumiller.ddcmp.build.kotlin
import me.bumiller.ddcmp.build.sourceSets
import me.bumiller.ddcmp.build.versionCatalog
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.util.Optional

private const val NamespacePrefix = "me.bumiller.ddcmp"

/**
 * Plugin for any library class.
 */
open class LibraryPlugin : DdcmpConventionPlugin() {

    private fun String.pathToNamespaceSuffix() = replace(":", ".")

    override fun ExtensionAware.configureExtensions(project: Project) {
        val versions = project.versionCatalog()

        kotlin {
            jvmToolchain(11)

            jvm()

            iosArm64().binaries.framework()
            iosSimulatorArm64().binaries.framework()

            android {
                namespace = NamespacePrefix + project.path.pathToNamespaceSuffix()

                compileSdk = versions.findIntVersion("android.compileSdk")
                minSdk = versions.findIntVersion("android.minSdk")

                androidResources {
                    enable = true
                }
            }

            configureDependencies(project)
        }
    }

    private fun KotlinMultiplatformExtension.configureDependencies(project: Project) {
        sourceSets.commonMain.get().applyBundles(project, commonMainBundles)
        sourceSets.commonTest.get().applyBundles(project, commonTestBundles)
        sourceSets.androidMain.get().applyBundles(project, androidMainBundles)
    }

    private fun KotlinSourceSet.applyBundles(project: Project, bundles: List<String>) {
        val versions = project.versionCatalog()

        bundles
            .map(versions::findBundle)
            .map(Optional<Provider<ExternalModuleDependencyBundle>>::get)
            .map(Provider<ExternalModuleDependencyBundle>::get)
            .flatten()
            .distinct()
            .forEach { dependency ->
                dependencies {
                    implementation(dependency)
                }
            }
    }

    /**
     * Dependency bundles to apply to 'commonMain'
     */
    open val commonMainBundles = listOf("library.common-main")

    /**
     * Dependency bundles to apply to 'commonTest'
     */
    open val commonTestBundles = listOf("library.common.test")

    /**
     * Dependency bundles to apply to 'androidMain'
     */
    open val androidMainBundles = listOf("library.android.main")

    override val pluginCatalogNames = listOf(
        "kotlin.multiplatform",
        "kotlin.android.library",
        "android.lint"
    )

}
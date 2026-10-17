class ComposeLibraryPlugin : LibraryPlugin() {

    override val pluginCatalogNames =
        super.pluginCatalogNames + listOf(
            "compose.multiplatform",
            "compose.compiler",
            "compose.hotreload"
        )

    override val commonMainBundles =
        super.commonMainBundles + listOf("compose.common.main")

    override val androidMainBundles =
        super.androidMainBundles + listOf("compose.android.main")

    override val jvmMainBundles =
        super.jvmMainBundles + listOf("compose.jvm.main")

    override val commonMainProjectDependencies =
        super.commonMainProjectDependencies + setOf(":ui:common")

}
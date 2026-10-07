package me.ddcmp.feature.export.navigation

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface ExportRoutes {

    /**
     * The configuration screen.
     */
    @Serializable
    data object Configuration: ExportRoutes

    /**
     * The result screen.
     */
    @Serializable
    data class Result(val exportId: Long): ExportRoutes

}
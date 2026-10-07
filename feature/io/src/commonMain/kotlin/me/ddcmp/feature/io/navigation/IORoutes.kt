package me.ddcmp.feature.io.navigation

import kotlinx.serialization.Serializable

/**
 * The import entries.
 */
internal sealed interface IORoutes {

    /**
     * Route for configuring the import.
     */
    @Serializable
    data object ImportConfig : IORoutes

    /**
     * Route for configuring the export.
     */
    @Serializable
    data object ExportConfig : IORoutes

    /**
     * Route for showing the result of an import action.
     */
    @Serializable
    data class ImportResult(val resultId: Long)

    /**
     * Route for showing the result of an export action.
     */
    @Serializable
    data class ExportResult(val resultId: Long)

}
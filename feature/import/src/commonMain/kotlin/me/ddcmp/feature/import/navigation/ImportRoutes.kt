package me.ddcmp.feature.import.navigation

import kotlinx.serialization.Serializable

/**
 * The import entries.
 */
internal sealed interface ImportRoutes {

    /**
     * Route for configuring the import.
     */
    @Serializable
    data object Configuration : ImportRoutes

    /**
     * Route for showing the result of an import action.
     */
    @Serializable
    data class Result(val resultId: Long)

}
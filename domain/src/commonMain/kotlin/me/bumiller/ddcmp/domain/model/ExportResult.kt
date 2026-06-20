package me.bumiller.ddcmp.domain.model

/**
 * Result when exporting data.
 */
sealed interface ExportResult {

    /**
     * Data was exported successfully.
     */
    data object Success: ExportResult

    /**
     * Failure arose when exporting.
     */
    sealed interface Failure: ExportResult {

        /**
         * The file could not be accessed.
         */
        data object FileAccess: Failure

    }

}
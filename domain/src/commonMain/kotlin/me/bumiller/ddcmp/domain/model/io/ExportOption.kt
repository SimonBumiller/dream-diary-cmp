package me.bumiller.ddcmp.domain.model.io

/**
 * Options for exporting the data into a backup.
 */
sealed interface ExportOption {

    /**
     * Normal data export.
     */
    data class Normal(

        /**
         * The file type that should be exported to.
         */
        val fileType: IOFileType

    ) : ExportOption

    /**
     * Export into the legacy format
     */
    data object Legacy : ExportOption

}
package me.bumiller.ddcmp.domain.model.io

/**
 * Options for exporting the data into a backup.
 */
public sealed interface ExportOption {

    /**
     * Normal data export.
     */
    public data class Normal(

        /**
         * The file type that should be exported to.
         */
        val fileType: IOFileType

    ) : ExportOption

    /**
     * Export into the legacy format
     */
    public data object Legacy : ExportOption

}
package me.bumiller.ddcmp.domain.model.io

import kotlinx.datetime.TimeZone
import me.bumiller.ddcmp.domain.model.io.IOFileType

public sealed interface ImportOption {

    /**
     * Whether favorite-marked dreams should carry that information.
     */
    public val transferFavorites: Boolean

    /**
     * The file extension of the backup file.
     */
    public val backupFileExtension: String

    /**
     * Importing legacy backups.
     */
    public data class Legacy(

        /**
         * The time zone to use when constructing the dreams.
         */
        val timeZone: TimeZone,

        /**
         * Whether relations should also be transferred.
         */
        val transferRelations: Boolean,

        override val transferFavorites: Boolean

    ) : ImportOption {

        override val backupFileExtension: String = "json"

    }

    /**
     * Importing common backups.
     */
    public data class Normal(

        /**
         * The filetype that the content is represented in.
         */
        val contentFileType: IOFileType,

        override val transferFavorites: Boolean

    ): ImportOption {

        override val backupFileExtension: String = "zip"

    }

}
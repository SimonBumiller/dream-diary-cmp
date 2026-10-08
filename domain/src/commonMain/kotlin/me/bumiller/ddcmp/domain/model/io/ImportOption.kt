package me.bumiller.ddcmp.domain.model.io

import kotlinx.datetime.TimeZone
import me.bumiller.ddcmp.domain.model.io.IOFileType

sealed interface ImportOption {

    /**
     * Whether favorite-marked dreams should carry that information.
     */
    val transferFavorites: Boolean

    /**
     * The file extension of the backup file.
     */
    val backupFileExtension: String

    /**
     * Importing legacy backups.
     */
    data class Legacy(

        /**
         * The time zone to use when constructing the dreams.
         */
        val timeZone: TimeZone,

        override val transferFavorites: Boolean

    ) : ImportOption {

        override val backupFileExtension = "json"

    }

    /**
     * Importing common backups.
     */
    data class Normal(

        /**
         * The filetype that the content is represented in.
         */
        val contentFileType: IOFileType,

        override val transferFavorites: Boolean

    ): ImportOption {

        override val backupFileExtension = "zip"

    }

}
package me.bumiller.ddcmp.domain.model

import kotlinx.datetime.TimeZone

sealed interface ImportOption {

    /**
     * Whether favorite-marked dreams should carry that information.
     */
    val transferFavorites: Boolean

    /**
     * The matching file extensions
     */
    val fileExtensions: List<String>

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

        override val fileExtensions: List<String>
            get() = listOf("json")

    }

    /**
     * Importing common backups.
     */
    data class Normal(

        /**
         * The filetype to import.
         */
        val fileType: IOFileType,

        override val transferFavorites: Boolean

    ): ImportOption {

        override val fileExtensions: List<String>
            get() = fileType.fileExtensions.toList()

    }

}
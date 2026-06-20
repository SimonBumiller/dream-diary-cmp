package me.bumiller.ddcmp.domain.model

/**
 * Types of files that can be imported.
 */
enum class IOFileType(

    /**
     * The matching file extension.
     */
    vararg val fileExtensions: String

) {

    /**
     * Json files.
     */
    Json("json"),

    /**
     * Xml files.
     */
    Xml("xml"),

    /**
     * Yaml files.
     */
    Yaml("yaml", "yml")

}
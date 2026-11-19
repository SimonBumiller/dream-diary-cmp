package me.bumiller.ddcmp.domain.model.io

/**
 * Types of files that can be imported.
 */
public enum class IOFileType(

    /**
     * The matching file extension.
     */
    public vararg val fileExtensions: String

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
    Yaml("yaml", "yml");

    public val exportFileEx: String
        get() = fileExtensions.first()

}
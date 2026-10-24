package me.bumiller.ddcmp.core.io.common

import no.synth.kmpzip.zip.ZipEntry
import no.synth.kmpzip.zip.ZipOutputStream

/**
 * Function to configure a specific entry inside a zip output stream.
 *
 * @param name The name of the entry
 * @param extension The file extension of the entry
 * @param block The configuration block
 */
internal inline fun ZipOutputStream.configureEntry(
    name: String,
    extension: String,
    block: ZipOutputStream.() -> Unit
) = configureEntry("$name.$extension", block)

/**
 * Function to configure a specific entry inside a zip output stream.
 *
 * @param name The name of the entry
 * @param block The configuration block
 */
internal inline fun ZipOutputStream.configureEntry(
    name: String,
    block: ZipOutputStream.() -> Unit
) {
    val entry = ZipEntry(name)

    putNextEntry(entry)
    try {
        block()
    } finally {
        closeEntry()
    }
}
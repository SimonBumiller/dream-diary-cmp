package me.bumiller.ddcmp.core.io.common

import kotlinx.io.Source
import kotlinx.io.buffered
import me.bumiller.ddcmp.domain.model.io.IOFileType
import no.synth.kmpzip.kotlinx.asSource
import no.synth.kmpzip.zip.ZipFile

internal fun ZipFile.sourceForName(name: String, fileType: IOFileType): Source? {
    val namesWithExtensions = fileType.fileExtensions
        .map { "$name.$it" }

    val entry = namesWithExtensions.firstNotNullOfOrNull { getEntry(it) } ?: return null
    return getInputStream(entry).asSource().buffered()
}
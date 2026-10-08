package me.bumiller.ddcmp.core.io.importer

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.io.Source
import kotlinx.io.buffered
import me.bumiller.ddcmp.core.io.common.asSeekableSource
import me.bumiller.ddcmp.domain.io.BackupImporter
import me.bumiller.ddcmp.domain.model.io.IOFileType
import me.bumiller.ddcmp.domain.model.io.ImportResult
import no.synth.kmpzip.kotlinx.asSource
import no.synth.kmpzip.zip.ZipFile

internal abstract class ZipFileImporter : BackupImporter {

    override suspend fun importBackup(file: PlatformFile): ImportResult {
        val seekableFile = file.asSeekableSource()

        return ZipFile(seekableFile).use { zipFile ->
            importBackup(zipFile)
        }
    }

    protected fun ZipFile.sourceForName(name: String, fileType: IOFileType): Source? {
        val namesWithExtensions = fileType.fileExtensions
            .map { "$name.$it" }

        val entry = namesWithExtensions.firstNotNullOfOrNull { getEntry(it) } ?: return null
        return getInputStream(entry).asSource().buffered()
    }

    protected abstract suspend fun importBackup(zipFile: ZipFile): ImportResult

}
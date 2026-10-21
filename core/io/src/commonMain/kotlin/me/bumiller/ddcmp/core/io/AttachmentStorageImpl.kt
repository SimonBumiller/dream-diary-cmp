package me.bumiller.ddcmp.core.io

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.createDirectories
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.sink
import io.github.vinceglb.filekit.source
import kotlinx.io.RawSource
import kotlinx.io.Source
import kotlinx.io.buffered
import me.bumiller.ddcmp.common.captureException
import me.bumiller.ddcmp.domain.io.AttachmentStorage

/**
 * Gets the directory in which attachments are stored,
 */
internal expect fun attachmentDirectory(): PlatformFile

internal class AttachmentStorageImpl : AttachmentStorage {

    private val directory = attachmentDirectory()

    init {
        if (!directory.exists()) {
            directory.createDirectories()
        }
    }

    override suspend fun save(id: Long, source: RawSource): Long {
        val file = PlatformFile(directory, id.toString())

        return source.buffered().transferTo(file.sink())
    }

    override suspend fun delete(id: Long): Boolean {
        val file = PlatformFile(directory, id.toString())

        try {
            file.delete(mustExist = true)
            return true
        } catch (e: Exception) {
            captureException(e)
            return false
        }
    }

    override suspend fun get(id: Long): Source? {
        val file = PlatformFile(directory, id.toString())

        return try {
            file.source().buffered()
        } catch (e: Exception) {
            captureException(e)
            null
        }
    }

}
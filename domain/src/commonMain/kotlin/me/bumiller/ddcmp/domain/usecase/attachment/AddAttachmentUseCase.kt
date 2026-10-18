package me.bumiller.ddcmp.domain.usecase.attachment

import kotlinx.io.Source
import me.bumiller.ddcmp.domain.infrastructure.AttachmentMetaRepository
import me.bumiller.ddcmp.domain.io.AttachmentStorage
import me.bumiller.ddcmp.domain.model.attachment.AttachmentMeta
import me.bumiller.ddcmp.domain.usecase.attachment.AddAttachmentUseCase.I
import me.bumiller.ddcmp.domain.usecase.meta.UseCase
import kotlin.time.Clock

/**
 * Usecase for adding an attachment to an existing dream.
 */
class AddAttachmentUseCase(
    private val attachmentMetaRepo: AttachmentMetaRepository,
    private val attachmentStorage: AttachmentStorage
): UseCase<I, AttachmentMeta> {

    override suspend fun invoke(i: I): AttachmentMeta {
        // Create initial meta, id and file size are temporary
        val now = Clock.System.now()
        val meta = AttachmentMeta(
            id = 0,
            created = now,
            edited = now,
            originalFilename = i.originalFileName,
            mimeType = i.mimeType,
            description = i.description,
            fileSize = -1,
            dreamId = i.dreamId
        ).let {
            val id = attachmentMetaRepo.save(it)
            it.copy(id = id)
        }

        // Save to storage
        val size = attachmentStorage.save(meta.id, i.source)
        val updatedMeta = meta.copy(fileSize = size)
        attachmentMetaRepo.save(updatedMeta)

        return updatedMeta
    }

    /**
     * Input for the usecase.
     */
    data class I(

        /**
         * The id of the dream.
         */
        val dreamId: Long,

        /**
         * The byte source.
         */
        val source: Source,

        /**
         * The name of the file the user selected.
         */
        val originalFileName: String,

        /**
         * Mime type as provided by the system.
         */
        val mimeType: String,

        /**
         * The name the user gave the attachment.
         */
        val name: String,

        /**
         * The description the user gave the attachment.
         */
        val description: String?

    )

}
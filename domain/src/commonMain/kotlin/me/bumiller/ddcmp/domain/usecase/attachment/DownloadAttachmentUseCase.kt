package me.bumiller.ddcmp.domain.usecase.attachment

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.sink
import io.github.vinceglb.filekit.source
import kotlinx.io.buffered
import me.bumiller.ddcmp.domain.io.AttachmentStorage
import me.bumiller.ddcmp.domain.usecase.attachment.DownloadAttachmentUseCase.I
import me.bumiller.ddcmp.domain.usecase.meta.UseCase

class DownloadAttachmentUseCase(
    private val attachmentStorage: AttachmentStorage
) : UseCase<I, Boolean> {

    override suspend fun invoke(i: I) = attachmentStorage
        .get(i.attachmentId)
        ?.source()
        ?.buffered()
        ?.transferTo(i.destination.sink())
        ?.let { it > 0 } != null

    /**
     * Input for the usecase.
     */
    data class I(

        /**
         * The id of the attachment.
         */
        val attachmentId: Long,

        /**
         * The destination to download to.
         */
        val destination: PlatformFile

    )

}
package me.bumiller.ddcmp.domain.usecase.attachment

import me.bumiller.ddcmp.domain.infrastructure.AttachmentMetaRepository
import me.bumiller.ddcmp.domain.io.AttachmentStorage
import me.bumiller.ddcmp.domain.usecase.meta.UseCase

/**
 * Usecase that removes a specified attachment.
 */
class RemoveAttachmentUseCase(
    private val attachmentMetaRepo: AttachmentMetaRepository,
    private val attachmentStorage: AttachmentStorage
) : UseCase<Long, Boolean> {

    override suspend fun invoke(i: Long): Boolean {
        // Use 'and' to ensure both are evaluated
        return attachmentMetaRepo.delete(i) and attachmentStorage.delete(i)
    }

}
package me.bumiller.ddcmp.domain.infrastructure

import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.attachment.AttachmentMeta

/**
 * Repository for attachments.
 */
interface AttachmentMetaRepository: Repository<AttachmentMeta> {

    /**
     * Gets all attachment-metas for a dream.
     */
    fun getForDream(dreamId: Long): Flow<List<AttachmentMeta>?>

}
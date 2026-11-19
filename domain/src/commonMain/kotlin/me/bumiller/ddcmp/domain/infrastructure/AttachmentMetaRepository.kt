package me.bumiller.ddcmp.domain.infrastructure

import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.attachment.AttachmentMeta

/**
 * Repository for attachments.
 */
public interface AttachmentMetaRepository: Repository<AttachmentMeta> {

    /**
     * Gets all attachment-metas for a dream.
     */
    public fun getForDream(dreamId: Long): Flow<List<AttachmentMeta>?>

    public suspend fun deleteForDream(dreamId: Long): Boolean

}
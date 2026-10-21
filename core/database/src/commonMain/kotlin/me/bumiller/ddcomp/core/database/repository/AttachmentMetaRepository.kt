package me.bumiller.ddcomp.core.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.domain.infrastructure.AttachmentMetaRepository
import me.bumiller.ddcmp.domain.model.attachment.AttachmentMeta
import me.bumiller.ddcomp.core.database.Mapping
import me.bumiller.ddcomp.core.database.dao.AttachmentMetaDao
import me.bumiller.ddcomp.core.database.entity.AttachmentMetaEntity
import me.bumiller.ddcomp.core.database.repository.base.SimpleBaseRepository

class AttachmentMetaRepository(
    private val metaDao: AttachmentMetaDao
) : AttachmentMetaRepository, SimpleBaseRepository<AttachmentMeta, AttachmentMetaEntity>(
    dao = metaDao,
    map = Mapping::attachmentMeta,
    reverseMap = Mapping.Reverse::attachmentMeta
) {

    override fun getForDream(dreamId: Long): Flow<List<AttachmentMeta>?> = metaDao
        .getAllForDream(dreamId)
        .map { it.map(Mapping::attachmentMeta) }

    override suspend fun deleteForDream(dreamId: Long) = metaDao
        .deleteForDream(dreamId) > 0

}
package me.bumiller.ddcmp.domain.usecase.attachment

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.io.Source
import me.bumiller.ddcmp.domain.infrastructure.AttachmentMetaRepository
import me.bumiller.ddcmp.domain.io.AttachmentStorage
import me.bumiller.ddcmp.domain.model.attachment.AttachmentMeta
import me.bumiller.ddcmp.domain.usecase.attachment.GetAttachmentsUseCase.I
import me.bumiller.ddcmp.domain.usecase.meta.FlowUseCase

/**
 * Usecase to get attachments.
 */
class GetAttachmentsUseCase(
    private val attachmentMetaRepo: AttachmentMetaRepository,
    private val attachmentStorage: AttachmentStorage
) : FlowUseCase<I, Unit> {

    override fun invoke(i: I): Flow<Unit> = metasBase(i)
        .map { metas ->
            metas.associateWith {
                attachmentStorage.get(it.id)
                    ?: throw IllegalStateException("Didn't find source for meta id ${it.id}")
            }
                .map { (meta, source) ->
                    O(meta, source)
                }
        }

    private fun metasBase(i: I) =
        if (i.id != null) attachmentMetaRepo
            .id(i.id)
            .map { it?.takeIf { i.dreamId == null || it.dreamId == i.dreamId } }
            .map { listOfNotNull(it) }
        else if (i.dreamId != null) attachmentMetaRepo
            .getForDream(i.dreamId)
            .map { it ?: emptyList() }
        else attachmentMetaRepo.all

    /**
     * The Query class.
     */
    data class I(

        /**
         * The id to filter for.
         */
        val id: Long? = null,

        /**
         * The dream id to filter for.
         */
        val dreamId: Long? = null

    )

    data class O(

        /**
         * The meta.
         */
        private val meta: AttachmentMeta,

        /**
         * The byte source.
         */
        private val source: Source

    )

}
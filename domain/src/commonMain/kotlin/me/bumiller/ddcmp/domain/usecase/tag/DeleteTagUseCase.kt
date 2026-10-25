package me.bumiller.ddcmp.domain.usecase.tag

import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.domain.infrastructure.TagRepository
import me.bumiller.ddcmp.domain.usecase.meta.UseCase

/**
 * Use case for deleting a tag.
 */
class DeleteTagUseCase(
    private val getTag: AllTagUseCase,
    private val tagRepo: TagRepository
) : UseCase<Long, AllTagUseCase.O?> {

    override suspend fun invoke(i: Long): AllTagUseCase.O? {
        val query = AllTagUseCase.I(id = i)
        val tag = getTag(query).first().firstOrNull() ?: return null

        return if (tagRepo.delete(i)) tag
        else null
    }

}
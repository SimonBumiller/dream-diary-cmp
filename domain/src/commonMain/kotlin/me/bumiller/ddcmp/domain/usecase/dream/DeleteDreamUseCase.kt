package me.bumiller.ddcmp.domain.usecase.dream

import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.domain.infrastructure.DreamRepository
import me.bumiller.ddcmp.domain.usecase.meta.UseCase

/**
 * Use case for deleting a dream.
 */
class DeleteDreamUseCase(
    private val getDReam: AllDreamUseCase,
    private val dreamRepo: DreamRepository
) : UseCase<Long, AllDreamUseCase.O?> {

    override suspend fun invoke(i: Long): AllDreamUseCase.O? {
        val query = AllDreamUseCase.I(id = i)
        val dream = getDReam(query).first().firstOrNull() ?: return null

        return if (dreamRepo.delete(i)) dream
        else null
    }

}
package me.bumiller.ddcmp.domain.usecase.person

import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.domain.infrastructure.PersonRepository
import me.bumiller.ddcmp.domain.usecase.meta.UseCase

/**
 * Use case for deleting a person.
 */
class DeletePersonUseCase(
    private val getPerson: AllPersonUseCase,
    private val personRepo: PersonRepository
) : UseCase<Long, AllPersonUseCase.O?> {

    override suspend fun invoke(i: Long): AllPersonUseCase.O? {
        val query = AllPersonUseCase.I(id = i)
        val person = getPerson(query).first().firstOrNull() ?: return null

        return if (personRepo.delete(i)) person
        else null
    }

}
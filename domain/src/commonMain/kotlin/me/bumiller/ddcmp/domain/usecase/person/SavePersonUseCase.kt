package me.bumiller.ddcmp.domain.usecase.person

import me.bumiller.ddcmp.domain.infrastructure.PersonRepository
import me.bumiller.ddcmp.domain.model.Person
import me.bumiller.ddcmp.domain.usecase.meta.UseCase
import kotlin.time.ExperimentalTime

class SavePersonUseCase(
    val repo: PersonRepository
) : UseCase<Person, Person> {

    @OptIn(ExperimentalTime::class)
    override suspend fun invoke(i: Person): Person {
        val id = repo.save(i)

        return i.copy(id = id)
    }

}
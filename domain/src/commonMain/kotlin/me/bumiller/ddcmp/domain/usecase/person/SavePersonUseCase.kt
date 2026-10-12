package me.bumiller.ddcmp.domain.usecase.person

import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.domain.infrastructure.PersonRepository
import me.bumiller.ddcmp.domain.infrastructure.TagRepository
import me.bumiller.ddcmp.domain.model.dream.Person
import me.bumiller.ddcmp.domain.model.dream.Tag
import me.bumiller.ddcmp.domain.usecase.meta.UseCase
import kotlin.time.ExperimentalTime

class SavePersonUseCase(
    val personRepo: PersonRepository,
    val tagRepo: TagRepository
) : UseCase<SavePersonUseCase.I, Person> {

    @OptIn(ExperimentalTime::class)
    override suspend fun invoke(i: I): Person {
        val person = personRepo.save(i.person).let {
            if(it == -1L) i.person
            else i.person.copy(id = it)
        }

        val existingTags = tagRepo.getForPerson(person.id).first() ?: emptyList()

        // Adding new tags

        val toAddTags = i.tags.filter { it.id !in existingTags.map(Tag::id) }

        for (tag in toAddTags) {
            tagRepo.addPersonTo(tag.id, person.id)
        }

        // Removing invalid tags

        val toRemoveTags = existingTags.filter { it.id !in i.tags.map(Tag::id) }

        for (tag in toRemoveTags) {
            tagRepo.removePersonFrom(tag.id, person.id)
        }

        return person
    }

    /**
     * Input for the use case.
     */
    data class I(

        /**
         * The person to save.
         */
        val person: Person,

        /**
         * The tags for that person.
         */
        val tags: List<Tag>

    )

}
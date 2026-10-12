package me.bumiller.ddcmp.domain.usecase.tag

import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.domain.infrastructure.DreamRepository
import me.bumiller.ddcmp.domain.infrastructure.PersonRepository
import me.bumiller.ddcmp.domain.infrastructure.TagRepository
import me.bumiller.ddcmp.domain.model.dream.Dream
import me.bumiller.ddcmp.domain.model.dream.Person
import me.bumiller.ddcmp.domain.model.dream.Tag
import me.bumiller.ddcmp.domain.usecase.meta.UseCase
import me.bumiller.ddcmp.domain.usecase.tag.SaveTagUseCase.I
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SaveTagUseCase(
    val tagRepo: TagRepository,
    val dreamRepo: DreamRepository,
    val personRepo: PersonRepository
) : UseCase<I, Tag> {

    override suspend fun invoke(i: I ): Tag {
        val tag = tagRepo.save(i.tag).let {
            if(it == -1L) i.tag
            else i.tag.copy(id = it)
        }

        val existingDreams = dreamRepo.getForTag(tag.id).first() ?: emptyList()
        val existingPersons = personRepo.getForTag(tag.id).first() ?: emptyList()

        // Adding new dreams and persons

        val toAddDreams = i.dreams.filter { it.id !in existingDreams.map(Dream::id) }
        val toAddPersons = i.persons.filter { it.id !in existingPersons.map(Person::id) }

        toAddDreams.forEach { dream ->
            tagRepo.addDreamTo(tag.id, dream.id)
        }
        toAddPersons.forEach { person ->
            tagRepo.addPersonTo(tag.id, person.id)
        }

        // Removing old dreams and persons

        val toRemoveDreams = existingDreams.filter { it.id !in i.dreams.map(Dream::id) }
        val toRemovePersons = existingPersons.filter { it.id !in i.persons.map(Person::id) }

        toRemoveDreams.forEach { dream ->
            tagRepo.removeDreamFrom(tag.id, dream.id)
        }
        toRemovePersons.forEach { person ->
            tagRepo.removePersonFrom(tag.id, person.id)
        }

        return tag
    }

    /**
     * Input for the usecase.
     */
    data class I(

        /**
         * The tag that should be saved.
         */
        val tag: Tag,

        /**
         * The persons that should be saved for the tag.
         */
        val persons: List<Person> = emptyList(),

        /**
         * The dreams that should be saved for the tag.
         */
        val dreams: List<Dream> = emptyList()

    )


}
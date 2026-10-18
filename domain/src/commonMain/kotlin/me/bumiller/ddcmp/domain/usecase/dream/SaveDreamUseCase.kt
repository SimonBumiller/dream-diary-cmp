package me.bumiller.ddcmp.domain.usecase.dream

import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.domain.infrastructure.DreamRepository
import me.bumiller.ddcmp.domain.infrastructure.EmotionRepository
import me.bumiller.ddcmp.domain.infrastructure.PersonRepository
import me.bumiller.ddcmp.domain.infrastructure.TagRepository
import me.bumiller.ddcmp.domain.model.dream.Dream
import me.bumiller.ddcmp.domain.model.dream.Person
import me.bumiller.ddcmp.domain.model.dream.Tag
import me.bumiller.ddcmp.domain.model.emotion.Emotion
import me.bumiller.ddcmp.domain.usecase.meta.UseCase

class SaveDreamUseCase(
    val dreamRepo: DreamRepository,
    val emotionRepo: EmotionRepository,
    val personRepo: PersonRepository,
    val tagRepo: TagRepository
) : UseCase<SaveDreamUseCase.I, Dream> {

    override suspend fun invoke(i: I): Dream {
        val dream = dreamRepo.save(i.dream).let {
            if (it == -1L) i.dream
            else i.dream.copy(id = it)
        }


        val existingEmotions = emotionRepo.getForDream(dream.id).first() ?: emptyList()
        val existingPersons = personRepo.getForDream(dream.id).first()
        val existingTags = tagRepo.getForDream(dream.id).first() ?: emptyList()

        // Adding new emotions and persons and tags

        val toAddEmotions = i.emotions.filter { it.id !in existingEmotions.map(Emotion::id) }
        val toAddPersons = i.persons.filter { it.id !in existingPersons.map(Person::id) }
        val toAddTags = i.tags.filter { it.id !in existingTags.map(Tag::id) }

        toAddEmotions.forEach { emotion ->
            dreamRepo.addEmotionTo(dream.id, emotion.id)
        }
        toAddPersons.forEach { person ->
            dreamRepo.addPersonTo(dream.id, person.id)
        }
        toAddTags.forEach { tag ->
            tagRepo.addDreamTo(tag.id, dream.id)
        }

        // Removing old emotions and persons and tags

        val toRemoveEmotions = existingEmotions.filter { it.id !in i.emotions.map(Emotion::id) }
        val toRemovePersons = existingPersons.filter { it.id !in i.persons.map(Person::id) }
        val toRemoveTags = existingTags.filter { it.id !in i.tags.map(Tag::id) }

        toRemoveEmotions.forEach { emotion ->
            dreamRepo.removeEmotionFrom(dream.id, emotion.id)
        }
        toRemovePersons.forEach { person ->
            dreamRepo.removePersonFrom(dream.id, person.id)
        }
        toRemoveTags.forEach { tag ->
            tagRepo.removeDreamFrom(tag.id, dream.id)
        }

        return dream
    }

    /**
     * Input for the usecase.
     */
    data class I(

        /**
         * The dream that should be saved.
         */
        val dream: Dream,

        /**
         * The persons that should be saved for the dream.
         */
        val persons: List<Person> = emptyList(),

        /**
         * The emotions that should be saved for the dream.
         */
        val emotions: List<Emotion> = emptyList(),

        /**
         * The tags that should be saved for the dream.
         */
        val tags: List<Tag> = emptyList()

    )


}
package me.bumiller.ddcmp.domain.usecase.dream

import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.domain.infrastructure.DreamRepository
import me.bumiller.ddcmp.domain.infrastructure.EmotionRepository
import me.bumiller.ddcmp.domain.infrastructure.PersonRepository
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.model.Emotion
import me.bumiller.ddcmp.domain.model.Person
import me.bumiller.ddcmp.domain.usecase.meta.UseCase
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SaveDreamUseCase(
    val dreamRepo: DreamRepository,
    val emotionRepo: EmotionRepository,
    val personRepo: PersonRepository
) : UseCase<SaveDreamUseCase.I, Dream> {

    override suspend fun invoke(i: I ): Dream {
        val dream = dreamRepo.save(i.dream).let {
            if(it == -1L) i.dream
            else i.dream.copy(id = it)
        }

        val existingEmotions = emotionRepo.getForDream(dream.id).first() ?: emptyList()
        val existingPersons = personRepo.getForDream(dream.id).first() ?: emptyList()

        // Adding new emotions and persons

        val toAddEmotions = i.emotions.filter { it.id !in existingEmotions.map(Emotion::id) }
        val toAddPersons = i.persons.filter { it.id !in existingPersons.map(Person::id) }

        toAddEmotions.forEach { emotion ->
            dreamRepo.addEmotionTo(dream.id, emotion.id)
        }
        toAddPersons.forEach { person ->
            dreamRepo.addPersonTo(dream.id, person.id)
        }

        // Removing old emotions and persons

        val toRemoveEmotions = existingEmotions.filter { it.id !in i.emotions.map(Emotion::id) }
        val toRemovePersons = existingPersons.filter { it.id !in i.persons.map(Person::id) }

        toRemoveEmotions.forEach { emotion ->
            dreamRepo.removeEmotionFrom(dream.id, emotion.id)
        }
        toRemovePersons.forEach { person ->
            dreamRepo.removePersonFrom(dream.id, person.id)
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
        val emotions: List<Emotion> = emptyList()

    )


}
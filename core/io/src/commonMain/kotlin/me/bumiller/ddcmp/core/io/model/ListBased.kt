@file:OptIn(ExperimentalTime::class)

package me.bumiller.ddcmp.core.io.model

import kotlinx.serialization.Serializable
import me.bumiller.ddcmp.domain.model.emotion.Arousal
import me.bumiller.ddcmp.domain.model.dream.Confidentiality
import me.bumiller.ddcmp.domain.model.emotion.Valence
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.domain.usecase.tag.AllTagUseCase
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import me.bumiller.ddcmp.domain.model.dream.Dream as ModelDream
import me.bumiller.ddcmp.domain.model.emotion.Emotion as ModelEmotion
import me.bumiller.ddcmp.domain.model.dream.Person as ModelPerson
import me.bumiller.ddcmp.domain.model.dream.Tag as ModelTag

internal object ListBased {

    @Serializable
    data class Dream(
        val id: Long,
        val created: Long,
        val edited: Long,
        val dreamt: Long,
        val title: String?,
        val description: String,
        val note: String?,
        val favorite: Boolean,
        val confidentiality: String
    ) {

        fun toModel() = ModelDream(id, Instant.fromEpochMilliseconds(created), Instant.fromEpochMilliseconds(edited),
            Instant.fromEpochMilliseconds(created), title, description, note, favorite,
            Confidentiality.valueOf(confidentiality))

        companion object {

            fun fromModel(model: ModelDream) = model.run {
                Dream(
                    id,
                    created.toEpochMilliseconds(),
                    edited.toEpochMilliseconds(),
                    dreamt.toEpochMilliseconds(),
                    title,
                    description,
                    note,
                    favorite,
                    confidentiality.name
                )
            }

        }

    }

    @Serializable
    data class Emotion(
        val id: Long,
        val created: Long,
        val edited: Long,
        val arousal: Float,
        val valence: Float,
        val name: String
    ) {

        companion object {
            fun fromModel(model: ModelEmotion) = model.run {
                Emotion(
                    id,
                    created.toEpochMilliseconds(),
                    edited.toEpochMilliseconds(),
                    arousal.value,
                    valence.value,
                    name
                )
            }
        }

        fun toModel() = ModelEmotion(
            id, Instant.fromEpochMilliseconds(created), Instant.fromEpochMilliseconds(edited),
            Arousal(arousal), Valence(valence), name
        )

    }

    @Serializable
    data class Tag(
        val id: Long,
        val created: Long,
        val edited: Long,
        val name: String,
        val description: String?,
        val color: Long,
    ) {

        companion object {
            fun fromModel(model: AllTagUseCase.O) = model.run {
                Tag(
                    tag.id,
                    tag.created.toEpochMilliseconds(),
                    tag.edited.toEpochMilliseconds(),
                    tag.name,
                    tag.description,
                    tag.color
                )
            }
        }

        fun toModel() = ModelTag(
            id, Instant.fromEpochMilliseconds(created), Instant.fromEpochMilliseconds(edited),
            name, description, color
        )

    }

    @Serializable
    data class Person(
        val id: Long,
        val created: Long,
        val edited: Long,
        val name: String,
        val note: String?
    ) {

        fun toModel() = ModelPerson(
            id,
            Instant.fromEpochMilliseconds(created),
            Instant.fromEpochMilliseconds(edited),
            name,
            note
        )

        companion object {
            fun fromModel(model: AllPersonUseCase.O) = model.run {
                Person(person.id, person.created.toEpochMilliseconds(), person.edited.toEpochMilliseconds(), person.name, person.note)
            }
        }

    }

    @Serializable
    data class DreamToPersons(
        val dreamId: Long,
        val personId: Long
    ) {

        companion object {
            fun fromModel(model: AllDreamUseCase.O) =
                model.persons.map { DreamToPersons(model.dream.id, it.id) }
        }

    }

    @Serializable
    data class DreamToTag(
        val dreamId: Long,
        val tagId: Long
    ) {

        companion object {
            fun fromModel(model: AllDreamUseCase.O) =
                model.tags.map { DreamToTag(model.dream.id, it.id) }
            fun fromModel(model: AllTagUseCase.O) =
                model.dreams.map { DreamToTag(it.id, model.tag.id) }
        }

    }

    @Serializable
    data class PersonToTag(
        val personId: Long,
        val tagId: Long
    ) {

        companion object {
            fun fromModel(model: AllPersonUseCase.O) =
                model.tags.map { PersonToTag(model.person.id, it.id) }
            fun fromModel(model: AllTagUseCase.O) =
                model.persons.map { PersonToTag(it.id, model.tag.id) }
        }

    }

    @Serializable
    data class DreamToEmotion(
        val dreamId: Long,
        val emotionId: Long
    ) {

        companion object {
            fun fromModel(model: AllDreamUseCase.O) =
                model.emotions.map { DreamToEmotion(model.dream.id, it.id) }
        }

    }

}
@file:OptIn(ExperimentalTime::class)

package me.bumiller.ddcmp.core.io.model

import kotlinx.serialization.Serializable
import me.bumiller.ddcmp.domain.model.Arousal
import me.bumiller.ddcmp.domain.model.Confidentiality
import me.bumiller.ddcmp.domain.model.Valence
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import me.bumiller.ddcmp.domain.model.Dream as ModelDream
import me.bumiller.ddcmp.domain.model.Emotion as ModelEmotion
import me.bumiller.ddcmp.domain.model.Person as ModelPerson

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
            fun fromModel(model: ModelPerson) = model.run {
                Person(id, created.toEpochMilliseconds(), edited.toEpochMilliseconds(), name, note)
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
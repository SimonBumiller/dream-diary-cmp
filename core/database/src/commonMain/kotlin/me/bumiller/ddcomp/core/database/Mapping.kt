package me.bumiller.ddcomp.core.database

import me.bumiller.ddcmp.domain.model.Arousal
import me.bumiller.ddcmp.domain.model.Confidentiality
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.model.Emotion
import me.bumiller.ddcmp.domain.model.Person
import me.bumiller.ddcmp.domain.model.Valence
import me.bumiller.ddcomp.core.database.entity.DreamEntity
import me.bumiller.ddcomp.core.database.entity.EmotionEntity
import me.bumiller.ddcomp.core.database.entity.PersonEntity
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object Mapping {

    object Reverse {

        fun person(model: Person) = with(model) {
            PersonEntity(
                id = id,
                created = created,
                updated = edited,
                name = name,
                note = note
            )
        }

        fun emotion(model: Emotion) = with(model) {
            EmotionEntity(
                id = id,
                created = created,
                updated = edited,
                arousal = arousal.value,
                valence = valence.value,
                name = name
            )
        }

        fun dream(model: Dream) = with(model) {
            DreamEntity(
                id = id,
                created = created,
                updated = edited,
                title = title,
                dreamt = dreamt,
                description = description,
                note = note,
                favorite = favorite,
                confidentiality = confidentiality.name
            )
        }

    }

    fun person(entity: PersonEntity) = with(entity) {
        Person(
            id = id,
            created = created,
            edited = updated,
            name = name,
            note = note
        )
    }

    fun emotion(entity: EmotionEntity) = with(entity) {
        Emotion(
            id = id,
            created = created,
            edited = updated,
            arousal = Arousal(arousal),
            valence = Valence(valence),
            name = name
        )
    }

    fun confidentiality(value: String) = Confidentiality.valueOf(value)

    fun dream(entity: DreamEntity) =
        with(entity) {
            Dream(
                id = id,
                created = created,
                edited = updated,
                dreamt = dreamt,
                title = title,
                description = description,
                note = note,
                favorite = favorite,
                confidentiality = confidentiality(confidentiality)
            )
        }

    fun dreamAggregate(aggregate: DreamEntity.Aggregate) =
        dream(aggregate.dream)

}
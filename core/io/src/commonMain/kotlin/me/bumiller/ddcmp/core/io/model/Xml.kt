package me.bumiller.ddcmp.core.io.model

import kotlinx.serialization.Serializable
import me.bumiller.ddcmp.domain.model.Arousal
import me.bumiller.ddcmp.domain.model.Confidentiality
import me.bumiller.ddcmp.domain.model.Valence
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import nl.adaptivity.xmlutil.serialization.XmlChildrenName
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import me.bumiller.ddcmp.domain.model.Dream as ModelDream
import me.bumiller.ddcmp.domain.model.Emotion as ModelEmotion
import me.bumiller.ddcmp.domain.model.Person as ModelPerson

@OptIn(ExperimentalTime::class)
internal object Xml {

    @Serializable
    @XmlSerialName("person")
    data class PersonRef(

        @XmlElement(false)
        val ref: Long
    )

    @Serializable
    @XmlSerialName("emotion")
    data class EmotionRef(

        @XmlElement(false)
        val ref: Long
    )

    @Serializable
    @XmlSerialName("dreams")
    data class DreamsRoot(
        val dreams: List<Dream>
    )

    @Serializable
    @XmlSerialName("dream")
    data class Dream(

        @XmlElement(false)
        val id: Long,

        @XmlElement(false)
        val created: Long,

        @XmlElement(false)
        val edited: Long,

        @XmlElement(false)
        val dreamt: Long,

        @XmlElement(false)
        val confidentiality: String,

        @XmlElement(false)
        val favorite: Boolean,

        @XmlElement(true)
        val title: String?,

        @XmlElement(true)
        val description: String,

        @XmlElement(true)
        val note: String?,

        @XmlChildrenName("person")
        val persons: List<PersonRef>,

        @XmlChildrenName("emotion")
        val emotions: List<EmotionRef>,
    ) {

        fun toModel() = ModelDream(
            id,
            Instant.fromEpochMilliseconds(created),
            Instant.fromEpochMilliseconds(edited),
            Instant.fromEpochMilliseconds(dreamt),
            title,
            description,
            note,
            favorite,
            Confidentiality.valueOf(confidentiality)
        )

        companion object {
            fun fromModel(model: AllDreamUseCase.O) = model.run {
                Dream(
                    dream.id,
                    dream.created.toEpochMilliseconds(),
                    dream.edited.toEpochMilliseconds(),
                    dream.dreamt.toEpochMilliseconds(),
                    dream.confidentiality.name,
                    dream.favorite,
                    dream.title,
                    dream.description,
                    dream.note,
                    persons.map { PersonRef(it.id) },
                    emotions.map { EmotionRef(it.id) },
                )
            }
        }

    }

    @Serializable
    @XmlSerialName("emotions")
    data class EmotionsRoot(
        val emotions: List<Xml.Emotion>
    )

    @Serializable
    @XmlSerialName("emotion")
    data class Emotion(

        @XmlElement(false)
        val id: Long,

        @XmlElement(false)
        val created: Long,

        @XmlElement(false)
        val edited: Long,

        @XmlElement(false)
        val arousal: Float,

        @XmlElement(false)
        val valence: Float,

        @XmlElement(true)
        val name: String
    ) {

        fun toModel() = ModelEmotion(
            id, Instant.fromEpochMilliseconds(created), Instant.fromEpochMilliseconds(edited),
            Arousal(arousal), Valence(valence), name
        )

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

    }

    @Serializable
    @XmlSerialName("persons")
    data class PersonsRoot(
        val persons: List<Xml.Person>
    )

    @Serializable
    @XmlSerialName("person")
    data class Person(

        @XmlElement(false)
        val id: Long,

        @XmlElement(false)
        val created: Long,

        @XmlElement(false)
        val edited: Long,

        @XmlElement(true)
        val name: String,

        @XmlElement(true)
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

}
package me.bumiller.ddcmp.core.io.model

import kotlinx.serialization.Serializable
import me.bumiller.ddcmp.domain.model.dream.Confidentiality
import me.bumiller.ddcmp.domain.model.emotion.Arousal
import me.bumiller.ddcmp.domain.model.emotion.Valence
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.domain.usecase.tag.AllTagUseCase
import nl.adaptivity.xmlutil.serialization.XmlChildrenName
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import kotlin.time.Instant
import me.bumiller.ddcmp.domain.model.attachment.AttachmentMeta as ModelAttachmentMeta
import me.bumiller.ddcmp.domain.model.dream.Dream as ModelDream
import me.bumiller.ddcmp.domain.model.dream.Person as ModelPerson
import me.bumiller.ddcmp.domain.model.dream.Tag as ModelTag
import me.bumiller.ddcmp.domain.model.emotion.Emotion as ModelEmotion

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
    @XmlSerialName("tag")
    data class TagRef(

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

        @XmlChildrenName("tag")
        val tags: List<TagRef>,

        @XmlChildrenName("attachmentMeta")
        val attachmentMetas: List<Xml.AttachmentMeta>
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
                    tags.map { TagRef(it.id) },
                    attachmentMetas.map(Xml.AttachmentMeta::fromModel)
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
    @XmlSerialName("attachmentMeta")
    data class AttachmentMeta(

        @XmlElement(false)
        val id: Long,

        @XmlElement(false)
        val created: Long,

        @XmlElement(false)
        val edited: Long,

        @XmlElement(true)
        val originalFilename: String,

        @XmlElement(true)
        val mimeType: String,

        @XmlElement(true)
        val description: String?,

        @XmlElement(true)
        val name: String,

        @XmlElement(false)
        val fileSize: Long
    ) {

        fun toModel(dreamId: Long) = ModelAttachmentMeta(
            id, Instant.fromEpochMilliseconds(created), Instant.fromEpochMilliseconds(edited),
            originalFilename, mimeType, description, name, fileSize, dreamId
        )

        companion object {
            fun fromModel(model: ModelAttachmentMeta) = model.run {
                AttachmentMeta(
                    id,
                    created.toEpochMilliseconds(),
                    edited.toEpochMilliseconds(),
                    originalFilename, mimeType, description, name, fileSize
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
        val note: String?,

        @XmlChildrenName("tag")
        val tags: List<TagRef>

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
                Person(
                    person.id,
                    person.created.toEpochMilliseconds(),
                    person.edited.toEpochMilliseconds(),
                    person.name,
                    person.note,
                    tags.map { TagRef(it.id) })
            }
        }

    }

    @Serializable
    @XmlSerialName("tags")
    data class TagsRoot(
        val tags: List<Xml.Tag>
    )

    @Serializable
    @XmlSerialName("tag")
    data class Tag(

        @XmlElement(false)
        val id: Long,

        @XmlElement(false)
        val created: Long,

        @XmlElement(false)
        val edited: Long,

        @XmlElement(true)
        val name: String,

        @XmlElement(true)
        val description: String?,

        @XmlElement(true)
        val color: Long
    ) {

        fun toModel() = ModelTag(
            id,
            Instant.fromEpochMilliseconds(created),
            Instant.fromEpochMilliseconds(edited),
            name,
            description,
            color
        )

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

    }

}
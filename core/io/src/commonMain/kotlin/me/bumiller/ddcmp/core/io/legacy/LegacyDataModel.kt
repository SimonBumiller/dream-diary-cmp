package me.bumiller.ddcmp.core.io.legacy

import kotlinx.serialization.Serializable

@Serializable
internal data class LegacyRoot(
    val dreams: List<LegacyDream>,
    val persons: List<LegacyPerson>,
    val locations: List<LegacyLocation>,
    val relations: List<LegacyRelation>,
    val dreamPersonCrossref: List<LegacyCrossref>,
    val dreamLocationCrossref: List<LegacyCrossref>,
    val personRelationCrossrefs: List<LegacyCrossref>
)

@Serializable
internal data class LegacyDream(
    val id: Long,
    val description: String,
    val note: String?,
    val isFavourite: Boolean,
    val created: String,
    val updated: String,
    val clearness: Double?,
    val happiness: Double?
)

@Serializable
internal data class LegacyPerson(
    val id: Long,
    val name: String,
    val isFavourite: Boolean,
    val notes: String?
)

@Serializable
internal data class LegacyLocation(
    val id: Long,
    val name: String,
    val coordinates: String,
    val notes: String?
)

@Serializable
internal data class LegacyRelation(
    val id: Long,
    val name: String,
    val color: Double,
    val notes: String
)

@Serializable
internal data class LegacyCrossref(
    val first: Long,
    val second: Long
)
package me.bumiller.ddcmp.core.io.model

import kotlinx.serialization.Serializable

internal object Legacy {

    @Serializable
    internal data class Root(
        val dreams: List<Dream>,
        val persons: List<Person>,
        val locations: List<Location>,
        val relations: List<Relation>,
        val dreamPersonCrossref: List<Crossref>,
        val dreamLocationCrossref: List<Crossref>,
        val personRelationCrossrefs: List<Crossref>
    )

    @Serializable
    internal data class Dream(
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
    internal data class Person(
        val id: Long,
        val name: String,
        val isFavourite: Boolean,
        val notes: String?
    )

    @Serializable
    internal data class Location(
        val id: Long,
        val name: String,
        val coordinates: String,
        val notes: String?
    )

    @Serializable
    internal data class Relation(
        val id: Long,
        val name: String,
        val color: Double,
        val notes: String
    )

    @Serializable
    internal data class Crossref(
        val first: Long,
        val second: Long
    )
}
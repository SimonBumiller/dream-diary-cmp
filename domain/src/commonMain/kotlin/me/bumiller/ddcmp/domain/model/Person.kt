package me.bumiller.ddcmp.domain.model

import me.bumiller.ddcmp.domain.model.meta.Editable
import me.bumiller.ddcmp.domain.model.meta.Searchable
import me.bumiller.ddcmp.domain.model.meta.Unique
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Models a person that can be part of a dream.
 */
@OptIn(ExperimentalTime::class)
data class Person(

    override val id: Long,
    override val created: Instant,
    override var edited: Instant,

    /**
     * The name of that person.
     */
    var name: String,

    /**
     * Additional notes about the person
     */
    var note: String?

): Unique, Editable, Searchable {

    override val searchableFields: List<String>
        get() = listOfNotNull(name, note)

}

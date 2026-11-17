package me.ddcmp.feature.person.navigation

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface PersonRoutes {

    @Serializable
    data object PersonList : PersonRoutes

    /**
     * Navigation route for the person detail screen.
     */
    @Serializable
    data class PersonDetail(
        val personId: Long
    ): PersonRoutes

    @Serializable
    data class AddPerson(
        val personId: Long? = null
    ) : PersonRoutes

}
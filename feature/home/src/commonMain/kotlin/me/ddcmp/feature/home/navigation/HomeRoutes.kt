package me.ddcmp.feature.home.navigation

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface HomeRoutes {

    @Serializable
    data object Overview : HomeRoutes

}
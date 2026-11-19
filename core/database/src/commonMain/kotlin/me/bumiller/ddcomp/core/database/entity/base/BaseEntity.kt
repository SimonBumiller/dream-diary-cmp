package me.bumiller.ddcomp.core.database.entity.base

import kotlin.time.Instant

internal interface BaseEntity {

    val id: Long

    val created: Instant

    val updated: Instant

}
package me.bumiller.ddcomp.core.database.entity.base

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
interface BaseEntity {

    val id: Long

    val created: Instant

    val updated: Instant

}
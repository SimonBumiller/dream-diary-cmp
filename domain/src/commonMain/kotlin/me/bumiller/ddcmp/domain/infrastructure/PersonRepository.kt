package me.bumiller.ddcmp.domain.infrastructure

import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.Person
import me.bumiller.ddcmp.domain.model.meta.SortMode

/**
 * Typed person repository.
 */
interface PersonRepository : Repository<Person>
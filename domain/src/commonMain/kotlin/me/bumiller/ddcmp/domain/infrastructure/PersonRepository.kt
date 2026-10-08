package me.bumiller.ddcmp.domain.infrastructure

import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.dream.Person

/**
 * Typed person repository.
 */
interface PersonRepository : Repository<Person> {

    fun getForDream(dreamId: Long): Flow<List<Person>?>

}
package me.bumiller.ddcmp.domain.infrastructure

import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.dream.Person

/**
 * Typed person repository.
 */
public interface PersonRepository : Repository<Person> {

    public fun getForDream(dreamId: Long): Flow<List<Person>>
    public fun getForTag(tagId: Long): Flow<List<Person>>

}
package me.bumiller.ddcomp.core.database.repository

import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.domain.infrastructure.PersonRepository
import me.bumiller.ddcmp.domain.model.dream.Person
import me.bumiller.ddcomp.core.database.Mapping
import me.bumiller.ddcomp.core.database.dao.DreamDao
import me.bumiller.ddcomp.core.database.dao.PersonDao
import me.bumiller.ddcomp.core.database.entity.PersonEntity
import me.bumiller.ddcomp.core.database.repository.base.SimpleBaseRepository

class PersonRepository(
    personDao: PersonDao,
    val dreamDao: DreamDao
): SimpleBaseRepository<Person, PersonEntity>(
    dao = personDao,
    map = Mapping::person,
    reverseMap = Mapping.Reverse::person
), PersonRepository {

    override fun getForDream(dreamId: Long) = dreamDao.getByIdAggregate(dreamId)
        .map { aggregate -> aggregate?.persons?.map(Mapping::person) }

}
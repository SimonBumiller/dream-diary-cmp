package me.bumiller.ddcomp.core.database.repository

import me.bumiller.ddcmp.domain.model.Person
import me.bumiller.ddcomp.core.database.Mapping
import me.bumiller.ddcomp.core.database.dao.PersonDao
import me.bumiller.ddcomp.core.database.entity.PersonEntity
import me.bumiller.ddcomp.core.database.repository.base.SimpleBaseRepository

class PersonRepository(
    personDao: PersonDao
): SimpleBaseRepository<Person, PersonEntity>(
    dao = personDao,
    map = Mapping::person,
    reverseMap = Mapping.Reverse::person
)
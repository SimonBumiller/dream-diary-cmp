package me.bumiller.ddcomp.core.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.domain.infrastructure.TagRepository
import me.bumiller.ddcmp.domain.model.dream.Tag
import me.bumiller.ddcomp.core.database.Mapping
import me.bumiller.ddcomp.core.database.dao.DreamDao
import me.bumiller.ddcomp.core.database.dao.PersonDao
import me.bumiller.ddcomp.core.database.dao.TagDao
import me.bumiller.ddcomp.core.database.dao.m2m.DreamTagM2MDao
import me.bumiller.ddcomp.core.database.dao.m2m.PersonTagM2MDao
import me.bumiller.ddcomp.core.database.entity.m2m.DreamTagM2M
import me.bumiller.ddcomp.core.database.entity.m2m.PersonTagM2M

class TagRepository(
    private val tagDao: TagDao,
    private val dreamDao: DreamDao,
    private val personDao: PersonDao,
    private val dreamTagM2mDao: DreamTagM2MDao,
    private val personTagM2MDao: PersonTagM2MDao
) : TagRepository {

    override fun getForDream(dreamId: Long): Flow<List<Tag>?> = dreamDao
        .getByIdAggregate(dreamId).map { it?.tags?.map(Mapping::tag) ?: emptyList() }

    override fun getForPerson(personId: Long): Flow<List<Tag>?> = personDao
        .getByIdAggregate(personId).map { it?.tags?.map(Mapping::tag) ?: emptyList() }

    override suspend fun addDreamTo(tagId: Long, dreamId: Long) =
        dreamTagM2mDao.insert(DreamTagM2M(dreamId, tagId))

    override suspend fun addPersonTo(tagId: Long, personId: Long) =
        personTagM2MDao.insert(PersonTagM2M(personId, tagId))

    override suspend fun removeDreamFrom(tagId: Long, dreamId: Long) {
        dreamTagM2mDao.delete(dreamId, tagId)
    }

    override suspend fun removePersonFrom(
        tagId: Long,
        personId: Long
    ) {
        personTagM2MDao.delete(tagId, personId)
    }

    override val all: Flow<List<Tag>> =
        tagDao.getAll()
            .map { it.map(Mapping::tag) }

    override fun id(id: Long): Flow<Tag?> = tagDao
        .getById(id)
        .map { it?.let(Mapping::tag) }

    override suspend fun save(t: Tag): Long = tagDao
        .upsert(Mapping.Reverse.tag(t))

    override suspend fun delete(id: Long): Boolean = tagDao
        .getById(id)
        .first()
        ?.let { tagDao.delete(it) > 0 } ?: false

    override suspend fun deleteAll() = tagDao
        .deleteAll()
}
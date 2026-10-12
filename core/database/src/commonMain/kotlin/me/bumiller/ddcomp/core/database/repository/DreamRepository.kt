package me.bumiller.ddcomp.core.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.domain.infrastructure.DreamRepository
import me.bumiller.ddcmp.domain.model.dream.Dream
import me.bumiller.ddcomp.core.database.Mapping
import me.bumiller.ddcomp.core.database.dao.DreamDao
import me.bumiller.ddcomp.core.database.dao.EmotionDao
import me.bumiller.ddcomp.core.database.dao.PersonDao
import me.bumiller.ddcomp.core.database.dao.TagDao
import me.bumiller.ddcomp.core.database.dao.m2m.DreamEmotionM2MDao
import me.bumiller.ddcomp.core.database.dao.m2m.DreamPersonM2MDao
import me.bumiller.ddcomp.core.database.entity.m2m.DreamEmotionM2M
import me.bumiller.ddcomp.core.database.entity.m2m.DreamPersonM2M

class DreamRepository(
    private val dreamDao: DreamDao,
    private val personDao: PersonDao,
    private val emotionDao: EmotionDao,
    private val tagDao: TagDao,
    private val dreamPersonM2MDao: DreamPersonM2MDao,
    private val dreamEmotionM2MDao: DreamEmotionM2MDao
) : DreamRepository {

    override val all: Flow<List<Dream>> = dreamDao.getAllAggregate()
        .map { aggregates ->
            aggregates.map(Mapping::dreamAggregate)
        }

    override fun id(id: Long): Flow<Dream?> = all.map { dreams ->
        dreams.singleOrNull { it.id == id }
    }

    override suspend fun save(t: Dream): Long {
        return dreamDao.upsert(Mapping.Reverse.dream(t))
    }

    override suspend fun delete(id: Long) = dreamDao.getById(id)
        .first()
        ?.let { dreamDao.delete(it) > 0 } ?: false

    override suspend fun deleteAll() {
        dreamDao.deleteAll()
    }

    override suspend fun addEmotionTo(dreamId: Long, emotionId: Long) {
        dreamEmotionM2MDao.insert(DreamEmotionM2M(dreamId, emotionId))
    }

    override suspend fun removeEmotionFrom(dreamId: Long, emotionId: Long) {
        dreamEmotionM2MDao.delete(dreamId, emotionId)
    }

    override fun getForEmotion(emotionId: Long): Flow<List<Dream>> = emotionDao
        .getByIdAggregate(emotionId).map { it?.dreams?.map(Mapping::dream) ?: emptyList() }

    override suspend fun addPersonTo(dreamId: Long, personId: Long) {
        dreamPersonM2MDao.insert(DreamPersonM2M(dreamId, personId))
    }

    override suspend fun removePersonFrom(dreamId: Long, personId: Long) {
        dreamPersonM2MDao.delete(dreamId, personId)
    }

    override fun getForPerson(personId: Long): Flow<List<Dream>> = personDao
        .getByIdAggregate(personId).map { it?.dreams?.map(Mapping::dream) ?: emptyList() }

    override fun getForTag(tagId: Long): Flow<List<Dream>> = tagDao
        .getByIdAggregate(tagId).map { it?.dreams?.map(Mapping::dream) ?: emptyList() }


}
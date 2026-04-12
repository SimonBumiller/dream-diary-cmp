package me.bumiller.ddcomp.core.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.domain.infrastructure.DreamRepository
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcomp.core.database.Mapping
import me.bumiller.ddcomp.core.database.dao.DreamDao

class DreamRepository(
    private val dreamDao: DreamDao
) : DreamRepository {

    override val all: Flow<List<Dream>> = dreamDao.getAllAggregate()
        .map { aggregates ->
            aggregates.map(Mapping::dreamAggregate)
        }

    override fun id(id: Long): Flow<Dream?> = all.map { dreams ->
        dreams.singleOrNull { it.id == id }
    }

    override suspend fun save(t: Dream) = dreamDao.upsert(Mapping.Reverse.dream(t))

    override suspend fun delete(id: Long) = dreamDao.getById(id)
        .first()
        ?.let { dreamDao.delete(it) > 0 } ?: false


}
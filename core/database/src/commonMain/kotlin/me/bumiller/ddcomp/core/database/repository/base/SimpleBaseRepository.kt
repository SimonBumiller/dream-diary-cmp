package me.bumiller.ddcomp.core.database.repository.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.meta.Unique
import me.bumiller.ddcomp.core.database.dao.base.BaseDao
import me.bumiller.ddcomp.core.database.entity.base.BaseEntity

open class SimpleBaseRepository<Model : Unique, Entity : BaseEntity>(

    private val dao: BaseDao<Entity>,

    private val map: (Entity) -> Model,

    private val reverseMap: (Model) -> Entity

) : Repository<Model> {

    override val all: Flow<List<Model>>
        get() = dao.getAll()
            .map { it.map(map) }

    override fun id(id: Long) = dao.getById(id)
        .map { it?.let(map) }

    override suspend fun save(t: Model) = dao.upsert(reverseMap(t))

    override suspend fun delete(id: Long) = dao.getById(id)
        .first()
        ?.let {
            dao.delete(it) > 0
        } ?: false

}
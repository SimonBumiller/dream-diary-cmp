package me.bumiller.ddcomp.core.database.dao.base

import kotlinx.coroutines.flow.Flow

interface AggregateDao<Entity, Aggregate> : BaseDao<Entity> {

    fun getByIdAggregate(id: Long): Flow<Aggregate?>

    fun getAllAggregate(): Flow<List<Aggregate>>

}
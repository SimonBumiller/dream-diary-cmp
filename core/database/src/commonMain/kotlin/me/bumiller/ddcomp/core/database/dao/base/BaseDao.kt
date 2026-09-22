package me.bumiller.ddcomp.core.database.dao.base

import kotlinx.coroutines.flow.Flow

interface BaseDao<Entity> {

    suspend fun upsert(entity: Entity): Long

    suspend fun delete(entity: Entity): Int

    fun getById(id: Long): Flow<Entity?>

    fun getAll(): Flow<List<Entity>>

    suspend fun deleteAll()

}
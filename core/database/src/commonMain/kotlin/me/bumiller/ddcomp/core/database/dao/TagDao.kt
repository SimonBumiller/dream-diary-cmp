package me.bumiller.ddcomp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcomp.core.database.dao.base.AggregateDao
import me.bumiller.ddcomp.core.database.entity.TagEntity

@Dao
interface TagDao: AggregateDao<TagEntity, TagEntity.Aggregate> {

    @Upsert
    override suspend fun upsert(entity: TagEntity): Long

    @Delete
    override suspend fun delete(entity: TagEntity): Int

    @Query("SELECT * FROM tags WHERE tags.id = :id")
    override fun getById(id: Long): Flow<TagEntity?>

    @Query("SELECT * FROM tags")
    override fun getAll(): Flow<List<TagEntity>>

    @Query("SELECT * FROM tags WHERE tags.id = :id")
    override fun getByIdAggregate(id: Long): Flow<TagEntity.Aggregate?>

    @Query("SELECT * FROM tags")
    override fun getAllAggregate(): Flow<List<TagEntity.Aggregate>>

    @Query("DELETE FROM tags")
    override suspend fun deleteAll()
}
package me.bumiller.ddcomp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcomp.core.database.dao.base.AggregateDao
import me.bumiller.ddcomp.core.database.entity.EmotionEntity

@Dao
interface EmotionDao: AggregateDao<EmotionEntity, EmotionEntity.Aggregate> {

    @Upsert
    override suspend fun upsert(entity: EmotionEntity): Long

    @Delete
    override suspend fun delete(entity: EmotionEntity): Int

    @Query("SELECT * FROM emotions WHERE emotions.id = :id")
    override fun getById(id: Long): Flow<EmotionEntity?>

    @Query("SELECT * FROM emotions")
    override fun getAll(): Flow<List<EmotionEntity>>

    @Transaction
    @Query("SELECT * FROM emotions")
    override fun getAllAggregate(): Flow<List<EmotionEntity.Aggregate>>

    @Transaction
    @Query("SELECT * FROM emotions WHERE emotions.id = :id")
    override fun getByIdAggregate(id: Long): Flow<EmotionEntity.Aggregate?>

    @Query("DELETE FROM dreams")
    override suspend fun deleteAll()
}
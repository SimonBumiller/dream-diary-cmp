package me.bumiller.ddcomp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.RoomRawQuery
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcomp.core.database.dao.base.AggregateDao
import me.bumiller.ddcomp.core.database.dao.base.BaseDao
import me.bumiller.ddcomp.core.database.entity.DreamEntity
import me.bumiller.ddcomp.core.database.entity.PersonEntity

@Dao
interface DreamDao: AggregateDao<DreamEntity, DreamEntity.Aggregate> {

    @Upsert
    override suspend fun upsert(entity: DreamEntity): Long

    @Delete
    override suspend fun delete(entity: DreamEntity): Int

    @Query("SELECT * FROM dreams WHERE dreams.id = :id")
    override fun getById(id: Long): Flow<DreamEntity?>

    @Query("SELECT * FROM dreams")
    override fun getAll(): Flow<List<DreamEntity>>

    @Query("SELECT * FROM dreams WHERE dreams.id = :id")
    override fun getByIdAggregate(id: Long): Flow<DreamEntity.Aggregate?>

    @Query("SELECT * FROM dreams")
    override fun getAllAggregate(): Flow<List<DreamEntity.Aggregate>>
}
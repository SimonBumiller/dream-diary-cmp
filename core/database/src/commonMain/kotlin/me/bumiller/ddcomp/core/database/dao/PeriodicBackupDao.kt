package me.bumiller.ddcomp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcomp.core.database.dao.base.AggregateDao
import me.bumiller.ddcomp.core.database.entity.PeriodicBackupEntity

@Dao
interface PeriodicBackupDao: AggregateDao<PeriodicBackupEntity, PeriodicBackupEntity.Aggregate> {

    @Query("SELECT * FROM periodic_backups WHERE id = :id")
    override fun getByIdAggregate(id: Long): Flow<PeriodicBackupEntity.Aggregate?>

    @Query("SELECT * FROM periodic_backups")
    override fun getAllAggregate(): Flow<List<PeriodicBackupEntity.Aggregate>>

    @Upsert
    override suspend fun upsert(entity: PeriodicBackupEntity): Long

    @Delete
    override suspend fun delete(entity: PeriodicBackupEntity): Int

    @Query("SELECT * FROM periodic_backups WHERE id = :id")
    override fun getById(id: Long): Flow<PeriodicBackupEntity?>

    @Query("SELECT * FROM periodic_backups")
    override fun getAll(): Flow<List<PeriodicBackupEntity>>

    @Query("DELETE FROM periodic_backups")
    override suspend fun deleteAll()

    @Query("SELECT * FROM periodic_backups WHERE orderId = :orderId")
    fun getAllAggregateForOrder(orderId: Long): Flow<List<PeriodicBackupEntity.Aggregate>>

}
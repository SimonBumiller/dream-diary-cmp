package me.bumiller.ddcomp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcomp.core.database.dao.base.AggregateDao
import me.bumiller.ddcomp.core.database.entity.PeriodicBackupOrderEntity
import me.bumiller.ddcomp.core.database.entity.PeriodicBackupOrderEntity.Aggregate

@Dao
interface PeriodicBackupOrderDao: AggregateDao<PeriodicBackupOrderEntity, Aggregate> {

    @Query("SELECT * FROM periodic_backup_orders WHERE id = :id")
    override fun getByIdAggregate(id: Long): Flow<Aggregate?>

    @Query("SELECT * FROM periodic_backup_orders")
    override fun getAllAggregate(): Flow<List<Aggregate>>

    @Upsert
    override suspend fun upsert(entity: PeriodicBackupOrderEntity): Long

    @Delete
    override suspend fun delete(entity: PeriodicBackupOrderEntity): Int

    @Query("SELECT * FROM periodic_backup_orders WHERE id = :id")
    override fun getById(id: Long): Flow<PeriodicBackupOrderEntity?>

    @Query("SELECT * FROM periodic_backup_orders")
    override fun getAll(): Flow<List<PeriodicBackupOrderEntity>>

    @Query("DELETE FROM periodic_backup_orders")
    override suspend fun deleteAll()

    @Query("SELECT * FROM periodic_backup_orders WHERE active = true")
    fun getActiveOrder(): Flow<Aggregate?>

}
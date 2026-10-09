package me.bumiller.ddcomp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcomp.core.database.dao.base.AggregateDao
import me.bumiller.ddcomp.core.database.dao.base.BaseDao
import me.bumiller.ddcomp.core.database.entity.BackupDestinationEntity
import me.bumiller.ddcomp.core.database.entity.DreamEntity

@Dao
interface BackupDestinationDao: BaseDao<BackupDestinationEntity> {

    @Upsert
    override suspend fun upsert(entity: BackupDestinationEntity): Long

    @Delete
    override suspend fun delete(entity: BackupDestinationEntity): Int

    @Query("SELECT * FROM backup_destinations WHERE backup_destinations.id = :id")
    override fun getById(id: Long): Flow<BackupDestinationEntity?>

    @Query("SELECT * FROM backup_destinations")
    override fun getAll(): Flow<List<BackupDestinationEntity>>

    @Query("DELETE FROM backup_destinations")
    override suspend fun deleteAll()
}
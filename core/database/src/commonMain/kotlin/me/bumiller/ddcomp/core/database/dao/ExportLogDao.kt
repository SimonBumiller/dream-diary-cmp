package me.bumiller.ddcomp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcomp.core.database.dao.base.BaseDao
import me.bumiller.ddcomp.core.database.entity.ExportLogEntity
import me.bumiller.ddcomp.core.database.entity.ImportLogEntity

@Dao
interface ExportLogDao : BaseDao<ExportLogEntity> {

    @Upsert
    override suspend fun upsert(entity: ExportLogEntity): Long

    @Delete
    override suspend fun delete(entity: ExportLogEntity): Int

    @Query("SELECT * FROM export_logs WHERE id = :id")
    override fun getById(id: Long): Flow<ExportLogEntity?>

    @Query("SELECT * FROM export_logs")
    override fun getAll(): Flow<List<ExportLogEntity>>

    @Query("DELETE FROM export_logs")
    override suspend fun deleteAll()
}
package me.bumiller.ddcomp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcomp.core.database.dao.base.BaseDao
import me.bumiller.ddcomp.core.database.entity.ImportLogEntity

@Dao
interface ImportLogDao : BaseDao<ImportLogEntity> {

    @Upsert
    override suspend fun upsert(entity: ImportLogEntity): Long

    @Delete
    override suspend fun delete(entity: ImportLogEntity): Int

    @Query("SELECT * FROM import_logs WHERE id = :id")
    override fun getById(id: Long): Flow<ImportLogEntity?>

    @Query("SELECT * FROM import_logs")
    override fun getAll(): Flow<List<ImportLogEntity>>

    @Query("DELETE FROM import_logs")
    override suspend fun deleteAll()
}
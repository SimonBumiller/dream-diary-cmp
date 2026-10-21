package me.bumiller.ddcomp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcomp.core.database.dao.base.BaseDao
import me.bumiller.ddcomp.core.database.entity.AttachmentMetaEntity

@Dao
interface AttachmentMetaDao : BaseDao<AttachmentMetaEntity> {

    @Upsert
    override suspend fun upsert(entity: AttachmentMetaEntity): Long

    @Delete
    override suspend fun delete(entity: AttachmentMetaEntity): Int

    @Query("SELECT * FROM attachment_metas WHERE id = :id")
    override fun getById(id: Long): Flow<AttachmentMetaEntity?>

    @Query("SELECT * FROM attachment_metas")
    override fun getAll(): Flow<List<AttachmentMetaEntity>>

    @Query("DELETE FROM attachment_metas")
    override suspend fun deleteAll()

    @Query("SELECT * FROM attachment_metas WHERE dreamId = :dreamId")
    fun getAllForDream(dreamId: Long): Flow<List<AttachmentMetaEntity>>

    @Query("DELETE FROM attachment_metas WHERE dreamId = :dreamId")
    suspend fun deleteForDream(dreamId: Long): Int

}
package me.bumiller.ddcomp.core.database.dao.m2m

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import me.bumiller.ddcomp.core.database.dao.base.M2MDao
import me.bumiller.ddcomp.core.database.entity.m2m.DreamEmotionM2M

@Dao
interface DreamEmotionM2MDao: M2MDao<DreamEmotionM2M> {

    @Insert
    override suspend fun insert(m2m: DreamEmotionM2M)

    @Query("DELETE FROM dreams_emotions_m2m WHERE dreamId = :first AND emotionId = :second")
    override suspend fun delete(first: Long, second: Long): Int

}
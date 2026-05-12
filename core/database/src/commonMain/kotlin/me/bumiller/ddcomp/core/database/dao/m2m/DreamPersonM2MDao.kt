package me.bumiller.ddcomp.core.database.dao.m2m

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import me.bumiller.ddcomp.core.database.dao.base.M2MDao
import me.bumiller.ddcomp.core.database.entity.m2m.DreamEmotionM2M
import me.bumiller.ddcomp.core.database.entity.m2m.DreamPersonM2M

@Dao
interface DreamPersonM2MDao: M2MDao<DreamPersonM2M> {

    @Insert
    override suspend fun insert(m2m: DreamPersonM2M)

    @Query("DELETE FROM dreams_persons_m2m WHERE dreamId = :first AND personId = :second")
    override suspend fun delete(first: Long, second: Long): Int

}
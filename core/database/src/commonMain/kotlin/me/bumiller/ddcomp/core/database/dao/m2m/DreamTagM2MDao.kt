package me.bumiller.ddcomp.core.database.dao.m2m

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import me.bumiller.ddcomp.core.database.dao.base.M2MDao
import me.bumiller.ddcomp.core.database.entity.m2m.DreamEmotionM2M
import me.bumiller.ddcomp.core.database.entity.m2m.DreamTagM2M

@Dao
interface DreamTagM2MDao: M2MDao<DreamTagM2M> {

    @Insert
    override suspend fun insert(m2m: DreamTagM2M)

    @Query("DELETE FROM dreams_tags_m2m WHERE dreamId = :first AND tagId = :second")
    override suspend fun delete(first: Long, second: Long): Int

}
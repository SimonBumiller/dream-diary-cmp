package me.bumiller.ddcomp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.bumiller.ddcomp.core.database.converter.InstantConverter
import me.bumiller.ddcomp.core.database.dao.DreamDao
import me.bumiller.ddcomp.core.database.dao.EmotionDao
import me.bumiller.ddcomp.core.database.dao.ImportLogDao
import me.bumiller.ddcomp.core.database.dao.PersonDao
import me.bumiller.ddcomp.core.database.dao.m2m.DreamEmotionM2MDao
import me.bumiller.ddcomp.core.database.dao.m2m.DreamPersonM2MDao
import me.bumiller.ddcomp.core.database.entity.DreamEntity
import me.bumiller.ddcomp.core.database.entity.EmotionEntity
import me.bumiller.ddcomp.core.database.entity.ImportLogEntity
import me.bumiller.ddcomp.core.database.entity.PersonEntity
import me.bumiller.ddcomp.core.database.entity.m2m.DreamEmotionM2M
import me.bumiller.ddcomp.core.database.entity.m2m.DreamPersonM2M

const val DB_NAME = "dream_diary.db"

private const val DB_VERSION = 3

/**
 * The relational database used to store the data of the app.
 */
@Database(
    version = DB_VERSION,
    entities = [
        DreamEntity::class,
        EmotionEntity::class,
        PersonEntity::class,
        ImportLogEntity::class,
        DreamEmotionM2M::class,
        DreamPersonM2M::class
    ]
)
@TypeConverters(InstantConverter::class)
abstract class RelationalDatabase : RoomDatabase() {

    abstract fun dreamDao(): DreamDao

    abstract fun emotionDao(): EmotionDao

    abstract fun personDao(): PersonDao

    abstract fun importLogDao(): ImportLogDao

    abstract fun dreamEmotionM2MDao(): DreamEmotionM2MDao
    abstract fun dreamPersonM2MDao(): DreamPersonM2MDao

}


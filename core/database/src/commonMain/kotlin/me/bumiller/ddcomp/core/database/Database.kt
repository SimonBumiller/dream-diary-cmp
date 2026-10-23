package me.bumiller.ddcomp.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.bumiller.ddcomp.core.database.converter.DurationConverter
import me.bumiller.ddcomp.core.database.converter.InstantConverter
import me.bumiller.ddcomp.core.database.dao.AttachmentMetaDao
import me.bumiller.ddcomp.core.database.dao.BackupDestinationDao
import me.bumiller.ddcomp.core.database.dao.DreamDao
import me.bumiller.ddcomp.core.database.dao.EmotionDao
import me.bumiller.ddcomp.core.database.dao.ExportLogDao
import me.bumiller.ddcomp.core.database.dao.ImportLogDao
import me.bumiller.ddcomp.core.database.dao.PeriodicBackupDao
import me.bumiller.ddcomp.core.database.dao.PeriodicBackupOrderDao
import me.bumiller.ddcomp.core.database.dao.PersonDao
import me.bumiller.ddcomp.core.database.dao.TagDao
import me.bumiller.ddcomp.core.database.dao.m2m.DreamEmotionM2MDao
import me.bumiller.ddcomp.core.database.dao.m2m.DreamPersonM2MDao
import me.bumiller.ddcomp.core.database.dao.m2m.DreamTagM2MDao
import me.bumiller.ddcomp.core.database.dao.m2m.PersonTagM2MDao
import me.bumiller.ddcomp.core.database.entity.AttachmentMetaEntity
import me.bumiller.ddcomp.core.database.entity.BackupDestinationEntity
import me.bumiller.ddcomp.core.database.entity.DreamEntity
import me.bumiller.ddcomp.core.database.entity.EmotionEntity
import me.bumiller.ddcomp.core.database.entity.ExportLogEntity
import me.bumiller.ddcomp.core.database.entity.ImportLogEntity
import me.bumiller.ddcomp.core.database.entity.PeriodicBackupEntity
import me.bumiller.ddcomp.core.database.entity.PeriodicBackupOrderEntity
import me.bumiller.ddcomp.core.database.entity.PersonEntity
import me.bumiller.ddcomp.core.database.entity.TagEntity
import me.bumiller.ddcomp.core.database.entity.m2m.DreamEmotionM2M
import me.bumiller.ddcomp.core.database.entity.m2m.DreamPersonM2M
import me.bumiller.ddcomp.core.database.entity.m2m.DreamTagM2M
import me.bumiller.ddcomp.core.database.entity.m2m.PersonTagM2M

const val DB_NAME = "dream_diary.db"

private const val DB_VERSION = 8

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
        ExportLogEntity::class,
        DreamEmotionM2M::class,
        DreamPersonM2M::class,
        PeriodicBackupEntity::class,
        PeriodicBackupOrderEntity::class,
        BackupDestinationEntity::class,
        DreamTagM2M::class,
        PersonTagM2M::class,
        TagEntity::class,
        AttachmentMetaEntity::class
    ],
    autoMigrations = [
        AutoMigration(from = 7, to = 8) // Migration for release 1.0.0|1.0.1|1.0.2 -> 1.1.0|1.1.1
    ]
)
@TypeConverters(InstantConverter::class, DurationConverter::class)
abstract class RelationalDatabase : RoomDatabase() {

    abstract fun dreamDao(): DreamDao

    abstract fun emotionDao(): EmotionDao

    abstract fun personDao(): PersonDao

    abstract fun tagDao(): TagDao

    abstract fun attachmentMetaDao(): AttachmentMetaDao

    abstract fun importLogDao(): ImportLogDao
    abstract fun exportLogDao(): ExportLogDao

    abstract fun periodicBackupDao(): PeriodicBackupDao
    abstract fun periodicBackupOrderDao(): PeriodicBackupOrderDao
    abstract fun backupDestinationDao(): BackupDestinationDao

    abstract fun dreamEmotionM2MDao(): DreamEmotionM2MDao
    abstract fun dreamPersonM2MDao(): DreamPersonM2MDao
    abstract fun dreamTagM2MDao(): DreamTagM2MDao
    abstract fun personTagM2MDao(): PersonTagM2MDao

}


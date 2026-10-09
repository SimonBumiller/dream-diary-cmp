package me.bumiller.ddcomp.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import androidx.room.Relation
import me.bumiller.ddcomp.core.database.entity.base.BaseEntity
import kotlin.time.Duration
import kotlin.time.Instant

@Entity(
    tableName = "periodic_backup_orders",
    foreignKeys = [
        ForeignKey(
            entity = BackupDestinationEntity::class,
            parentColumns = ["id"],
            childColumns = ["backupDestinationId"],
            onDelete = CASCADE
        )
    ]
)
data class PeriodicBackupOrderEntity(

    @PrimaryKey(autoGenerate = true)
    override val id: Long,

    override val created: Instant,

    override val updated: Instant,

    val backupDestinationId: Long,

    val active: Boolean,

    val wasActive: Boolean,

    val period: Duration,

    val initialTime: Instant,

    val keepLast: Int?,

    val legacyExport: Boolean,

    val exportFileType: String?

): BaseEntity {

    data class Aggregate(

        @Embedded
        val backupOrder: PeriodicBackupOrderEntity,

        @Relation(
            parentColumn = "backupDestinationId",
            entityColumn = "id"
        )
        val backupDestination: BackupDestinationEntity

    )

}

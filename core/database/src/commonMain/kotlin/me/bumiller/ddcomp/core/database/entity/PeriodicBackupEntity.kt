package me.bumiller.ddcomp.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import me.bumiller.ddcomp.core.database.entity.base.BaseEntity
import kotlin.time.Instant

@Entity(
    tableName = "periodic_backups",
    foreignKeys = [
        ForeignKey(
            entity = PeriodicBackupOrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["orderId"]
        ),
        ForeignKey(
            entity = ExportLogEntity::class,
            parentColumns = ["id"],
            childColumns = ["exportId"]
        )
    ]
)
data class PeriodicBackupEntity(

    @PrimaryKey(autoGenerate = true)
    override val id: Long,

    override val created: Instant,

    override val updated: Instant,

    val orderId: Long,

    val exportId: Long

): BaseEntity {

    data class Aggregate(

        @Embedded
        val backup: PeriodicBackupEntity,

        @Relation(
            parentColumn = "orderId",
            entityColumn = "id"
        )
        val order: PeriodicBackupOrderEntity,

        @Relation(
            parentColumn = "exportId",
            entityColumn = "id"
        )
        val export: ExportLogEntity

    )

}
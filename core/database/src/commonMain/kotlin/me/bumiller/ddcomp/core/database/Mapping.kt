package me.bumiller.ddcomp.core.database

import me.bumiller.ddcmp.domain.model.attachment.AttachmentMeta
import me.bumiller.ddcmp.domain.model.backup.PeriodicBackup
import me.bumiller.ddcmp.domain.model.backup.PeriodicBackupOrder
import me.bumiller.ddcmp.domain.model.dream.Confidentiality
import me.bumiller.ddcmp.domain.model.dream.Dream
import me.bumiller.ddcmp.domain.model.dream.Person
import me.bumiller.ddcmp.domain.model.dream.Tag
import me.bumiller.ddcmp.domain.model.emotion.Arousal
import me.bumiller.ddcmp.domain.model.emotion.Emotion
import me.bumiller.ddcmp.domain.model.emotion.Valence
import me.bumiller.ddcmp.domain.model.io.ExportOption
import me.bumiller.ddcmp.domain.model.io.ExportResult
import me.bumiller.ddcmp.domain.model.io.IOFileType
import me.bumiller.ddcmp.domain.model.io.ImportResult
import me.bumiller.ddcomp.core.database.entity.AttachmentMetaEntity
import me.bumiller.ddcomp.core.database.entity.DreamEntity
import me.bumiller.ddcomp.core.database.entity.EmotionEntity
import me.bumiller.ddcomp.core.database.entity.ExportLogEntity
import me.bumiller.ddcomp.core.database.entity.ImportLogEntity
import me.bumiller.ddcomp.core.database.entity.PeriodicBackupEntity
import me.bumiller.ddcomp.core.database.entity.PeriodicBackupOrderEntity
import me.bumiller.ddcomp.core.database.entity.PersonEntity
import me.bumiller.ddcomp.core.database.entity.TagEntity

object Mapping {

    object Reverse {

        fun periodicBackup(model: PeriodicBackup) = model.run {
            PeriodicBackupEntity(
                id = id,
                created = created,
                updated = created,
                orderId = model.order.id,
                exportId = model.export.id
            )
        }

        fun attachmentMeta(model: AttachmentMeta) = model.run {
            AttachmentMetaEntity(
                id,
                created,
                edited,
                originalFilename,
                mimeType,
                description,
                name,
                fileSize,
                dreamId
            )
        }

        fun tag(model: Tag) = model.run {
            TagEntity(
                id = id,
                created = created,
                updated = edited,
                name = name,
                description = description,
                color = color
            )
        }

        fun backupOrder(model: PeriodicBackupOrder) = model.run {
            PeriodicBackupOrderEntity(
                id = id,
                created = created,
                updated = created,
                backupDestinationId = model.destination.id,
                active = model.active,
                period = model.period,
                initialTime = initialTime,
                keepLast = keepLast,
                legacyExport = exportOption is ExportOption.Legacy,
                exportFileType = (exportOption as? ExportOption.Normal)?.fileType?.name,
                wasActive = wasActive
            )
        }

        fun person(model: Person) = with(model) {
            PersonEntity(
                id = id,
                created = created,
                updated = edited,
                name = name,
                note = note
            )
        }

        fun emotion(model: Emotion) = with(model) {
            EmotionEntity(
                id = id,
                created = created,
                updated = edited,
                arousal = arousal.value,
                valence = valence.value,
                name = name
            )
        }

        fun import(model: ImportResult) = with(model) {
            ImportLogEntity(
                id = id,
                created = started,
                updated = started,
                isFileAccessFailure = this is ImportResult.Failure.FileAccess,
                isDataConsistencyFailure = this is ImportResult.Failure.DataConsistency,
                isFileFormatFailure = this is ImportResult.Failure.FileFormat,
                dreamCount = (this as? ImportResult.Success)?.dreamCount ?: 0,
                personCount = (this as? ImportResult.Success)?.personCount ?: 0,
                firstDream = (this as? ImportResult.Success)?.firstDream ?: started,
                lastDream = (this as? ImportResult.Success)?.lastDream ?: started,
                isNormal = this is ImportResult.Success.Normal,
                emotionCount = (this as? ImportResult.Success.Normal)?.emotionCount ?: 0,
                ended = ended
            )
        }

        fun export(model: ExportResult) = with(model) {
            ExportLogEntity(
                id = id,
                created = started,
                updated = started,
                isFileAccessFailure = this is ExportResult.Failure.FileAccess,
                fileSize = (this as? ExportResult.Success)?.fileSize,
                fileLocation = (this as? ExportResult.Success)?.fileLocation,
                ended = ended
            )
        }

        fun dream(model: Dream) = with(model) {
            DreamEntity(
                id = id,
                created = created,
                updated = edited,
                title = title,
                dreamt = dreamt,
                description = description,
                note = note,
                favorite = favorite,
                confidentiality = confidentiality.name
            )
        }

    }

    fun attachmentMeta(
        entity: AttachmentMetaEntity
    ) = entity.run {
        AttachmentMeta(id, created, updated, fileName, mimeType, description, name, size, dreamId)
    }

    fun periodicBackup(
        aggregate: PeriodicBackupEntity.Aggregate,
        orderAggregate: PeriodicBackupOrderEntity.Aggregate
    ) = aggregate.run {
        PeriodicBackup(
            id = aggregate.backup.id,
            created = aggregate.backup.created,
            order = backupOrder(orderAggregate),
            export = export(aggregate.export)
        )
    }

    fun tag(entity: TagEntity) = entity.run {
        Tag(
            id = id,
            created = created,
            edited = updated,
            name = name,
            description = description,
            color = color
        )
    }

    fun backupOrder(aggregate: PeriodicBackupOrderEntity.Aggregate) = aggregate.run {
        PeriodicBackupOrder(
            id = aggregate.backupOrder.id,
            created = aggregate.backupOrder.created,
            destination = PlatformMapping.Reverse.backupDestination(aggregate.backupDestination),
            active = aggregate.backupOrder.active,
            period = aggregate.backupOrder.period,
            initialTime = aggregate.backupOrder.initialTime,
            keepLast = aggregate.backupOrder.keepLast,
            exportOption = when (aggregate.backupOrder.legacyExport) {
                true -> ExportOption.Legacy
                false -> ExportOption.Normal(IOFileType.valueOf(aggregate.backupOrder.exportFileType!!))
            },
            wasActive = aggregate.backupOrder.wasActive
        )
    }

    fun person(entity: PersonEntity) = with(entity) {
        Person(
            id = id,
            created = created,
            edited = updated,
            name = name,
            note = note
        )
    }

    fun emotion(entity: EmotionEntity) = with(entity) {
        Emotion(
            id = id,
            created = created,
            edited = updated,
            arousal = Arousal(arousal),
            valence = Valence(valence),
            name = name
        )
    }

    fun import(entity: ImportLogEntity) = with(entity) {
        if (isFileAccessFailure) ImportResult.Failure.FileAccess(id, created, ended)
        else if (isFileFormatFailure) ImportResult.Failure.FileFormat(id, created, ended)
        else if (isDataConsistencyFailure) ImportResult.Failure.DataConsistency(id, created, ended)
        else if (isNormal) ImportResult.Success.Normal(
            emotionCount = emotionCount,
            dreamCount = dreamCount,
            personCount = personCount,
            firstDream = firstDream,
            lastDream = lastDream,
            id = id,
            started = created,
            ended = ended
        ) else ImportResult.Success.Legacy(
            dreamCount = dreamCount,
            personCount = personCount,
            firstDream = firstDream,
            lastDream = lastDream,
            id = id,
            started = created,
            ended = ended
        )
    }

    fun export(entity: ExportLogEntity) = with(entity) {
        if (isFileAccessFailure) ExportResult.Failure.FileAccess(id, created, ended)
        else ExportResult.Success(
            fileSize = fileSize!!,
            fileLocation = fileLocation!!,
            id = id,
            started = created,
            ended = ended
        )
    }

    fun confidentiality(value: String) = Confidentiality.valueOf(value)

    fun dream(entity: DreamEntity) =
        with(entity) {
            Dream(
                id = id,
                created = created,
                edited = updated,
                dreamt = dreamt,
                title = title,
                description = description,
                note = note,
                favorite = favorite,
                confidentiality = confidentiality(confidentiality)
            )
        }

    fun dreamAggregate(aggregate: DreamEntity.Aggregate) =
        dream(aggregate.dream)

}
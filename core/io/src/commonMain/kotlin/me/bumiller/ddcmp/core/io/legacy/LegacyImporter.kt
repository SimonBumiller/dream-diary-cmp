package me.bumiller.ddcmp.core.io.legacy

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readString
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.serialization.json.Json
import me.bumiller.ddcmp.core.io.model.Legacy
import me.bumiller.ddcmp.domain.io.BackupImporter
import me.bumiller.ddcmp.domain.model.Confidentiality
import me.bumiller.ddcmp.domain.model.ImportResult
import me.bumiller.ddcomp.core.database.dao.DreamDao
import me.bumiller.ddcomp.core.database.dao.PersonDao
import me.bumiller.ddcomp.core.database.dao.m2m.DreamPersonM2MDao
import me.bumiller.ddcomp.core.database.entity.DreamEntity
import me.bumiller.ddcomp.core.database.entity.PersonEntity
import me.bumiller.ddcomp.core.database.entity.m2m.DreamPersonM2M
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class LegacyImporter(
    private val timeZone: TimeZone,
    private val transferFavorites: Boolean,
    private val defaultConfidentiality: Confidentiality,
    private val dreamDao: DreamDao,
    private val personDao: PersonDao,
    private val dreamPersonM2MDao: DreamPersonM2MDao
) : BackupImporter {

    @OptIn(ExperimentalTime::class)
    override suspend fun importBackup(file: PlatformFile): ImportResult {
        val now = Clock.System.now()

        val text = try {
            file.readString()
        } catch (_: Exception) {
            return ImportResult.Failure.FileAccess(0, now)
        }

        val root = try {
            Json.decodeFromString<Legacy.Root>(text)
        } catch (_: Exception) {
            return ImportResult.Failure.FileFormat(0, now)
        }

        // Check data integrity
        root.dreamPersonCrossref.forEach { crossref ->
            val dreamId = crossref.first
            val personId = crossref.second

            val dreamExists = root.dreams.any { it.id == dreamId }
            val personExists = root.persons.any { it.id == personId }

            if(!dreamExists || !personExists)
                return ImportResult.Failure.DataConsistency(0, now)
        }

        val dreams = root.dreams
            .map { legacyDream ->
                DreamEntity(
                    id = legacyDream.id,
                    created = LocalDate.parse(legacyDream.created).atStartOfDayIn(timeZone),
                    updated = LocalDate.parse(legacyDream.updated).atStartOfDayIn(timeZone),
                    dreamt = LocalDate.parse(legacyDream.created).atStartOfDayIn(timeZone),
                    title = null,
                    description = legacyDream.description,
                    note = legacyDream.note,
                    favorite = transferFavorites && legacyDream.isFavourite,
                    confidentiality = defaultConfidentiality.name
                )
            }

        val persons = root.persons
            .map { legacyPerson ->
                PersonEntity(
                    id = legacyPerson.id,
                    created = now,
                    updated = now,
                    name = legacyPerson.name,
                    note = legacyPerson.notes
                )
            }

        dreams.forEach { dream ->
            dreamDao.upsert(dream)
        }

        persons.forEach { person ->
            personDao.upsert(person)
        }

        root.dreamPersonCrossref.forEach { crossref ->
            val m2m = DreamPersonM2M(
                dreamId = crossref.first,
                personId = crossref.second
            )

            dreamPersonM2MDao.insert(m2m)
        }

        val result = ImportResult.Success.Legacy(
            dreamCount = dreams.size,
            personCount = persons.size,
            firstDream = dreams.minOf { it.dreamt },
            lastDream = dreams.maxOf { it.dreamt },
            id = 0,
            timestamp = now
        )

        return result
    }

}
package me.bumiller.ddcmp.core.io.legacy

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.writeString
import kotlinx.coroutines.flow.first
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import me.bumiller.ddcmp.domain.io.BackupExporter
import me.bumiller.ddcmp.domain.model.ExportResult
import me.bumiller.ddcomp.core.database.dao.DreamDao
import me.bumiller.ddcomp.core.database.dao.PersonDao
import kotlin.time.ExperimentalTime

internal class LegacyExporter(
    private val dreamDao: DreamDao,
    private val personDao: PersonDao
) : BackupExporter {

    @OptIn(ExperimentalTime::class)
    override suspend fun export(file: PlatformFile): ExportResult {
        val timeZone = TimeZone.currentSystemDefault()

        val legacyDreams = dreamDao.getAll()
            .first()
            .map { dream ->
                LegacyDream(
                    id = dream.id,
                    description = dream.description,
                    note = dream.note,
                    isFavourite = dream.favorite,
                    created = dream.dreamt.toLocalDateTime(timeZone).date.toString(),
                    updated = dream.updated.toLocalDateTime(timeZone).date.toString(),
                    clearness = null,
                    happiness = null
                )
            }

        val legacyPersons = personDao.getAll()
            .first()
            .map { person ->
                LegacyPerson(
                    id = person.id,
                    name = person.name,
                    isFavourite = false,
                    notes = person.note
                )
            }

        val dreamPersonCrossrefs = dreamDao.getAllAggregate()
            .first()
            .map { aggregate ->
                aggregate.dream.id to aggregate.persons.map { it.id }
            }
            .flatMap { pair ->
                pair.second.map { personId ->
                    pair.first to personId
                }
            }
            .map { pair ->
                LegacyCrossref(pair.first, pair.second)
            }

        val root = LegacyRoot(
            dreams = legacyDreams,
            persons = legacyPersons,
            locations = emptyList(),
            relations = emptyList(),
            dreamPersonCrossref = dreamPersonCrossrefs,
            dreamLocationCrossref = emptyList(),
            personRelationCrossrefs = emptyList()
        )

        val json = Json.encodeToString(root)
        file.writeString(json)

        return ExportResult.Success
    }
}
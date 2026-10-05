package me.bumiller.ddcmp.core.io.exporter

import kotlinx.coroutines.flow.first
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.io.buffered
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.io.encodeToSink
import me.bumiller.ddcmp.core.io.base.ZipFileExporter
import me.bumiller.ddcmp.core.io.model.Legacy
import me.bumiller.ddcmp.domain.model.ExportResult
import me.bumiller.ddcomp.core.database.dao.DreamDao
import me.bumiller.ddcomp.core.database.dao.PersonDao
import no.synth.kmpzip.kotlinx.asSink
import no.synth.kmpzip.zip.ZipEntry
import no.synth.kmpzip.zip.ZipOutputStream
import kotlin.time.ExperimentalTime

private const val FILE_NAME = "data.json"

/**
 * Backup Exporter implementation
 */
internal class LegacyExporter(
    private val dreamDao: DreamDao,
    private val personDao: PersonDao
) : ZipFileExporter() {

    @OptIn(ExperimentalTime::class, ExperimentalSerializationApi::class)
    override suspend fun aggregateZipFile(zipStream: ZipOutputStream): ExportResult {
        val timeZone = TimeZone.currentSystemDefault()

        val legacyDreams = dreamDao.getAll()
            .first()
            .map { dream ->
                Legacy.Dream(
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
                Legacy.Person(
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
                Legacy.Crossref(pair.first, pair.second)
            }

        val root = Legacy.Root(
            dreams = legacyDreams,
            persons = legacyPersons,
            locations = emptyList(),
            relations = emptyList(),
            dreamPersonCrossref = dreamPersonCrossrefs,
            dreamLocationCrossref = emptyList(),
            personRelationCrossrefs = emptyList()
        )

        zipStream.putNextEntry(ZipEntry(FILE_NAME))

        Json.encodeToSink(root, zipStream.asSink().buffered())

        zipStream.closeEntry()

        return ExportResult.Success
    }
}
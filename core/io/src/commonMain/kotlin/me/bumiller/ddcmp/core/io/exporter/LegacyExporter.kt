package me.bumiller.ddcmp.core.io.exporter

import kotlinx.coroutines.flow.first
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.io.IOException
import kotlinx.io.buffered
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.io.encodeToSink
import me.bumiller.ddcmp.core.io.model.Legacy
import me.bumiller.ddcmp.domain.model.io.ExportResult
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import no.synth.kmpzip.kotlinx.asSink
import no.synth.kmpzip.zip.ZipEntry
import no.synth.kmpzip.zip.ZipOutputStream
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val FILE_NAME = "data.json"

/**
 * Backup Exporter implementation
 */
internal class LegacyExporter(
    private val allDreams: AllDreamUseCase,
    private val allPersons: AllPersonUseCase
) : ZipFileExporter() {

    @OptIn(ExperimentalTime::class, ExperimentalSerializationApi::class)
    override suspend fun aggregateZipFile(zipStream: ZipOutputStream): ExportResult? {
        val startTimestamp = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()

        val allDreams = allDreams(AllDreamUseCase.I())

        val legacyDreams = allDreams
            .first()
            .map { (dream, _, _) ->
                Legacy.Dream(
                    id = dream.id,
                    description = dream.description,
                    note = dream.note,
                    isFavourite = dream.favorite,
                    created = dream.dreamt.toLocalDateTime(timeZone).date.toString(),
                    updated = dream.edited.toLocalDateTime(timeZone).date.toString(),
                    clearness = null,
                    happiness = null
                )
            }

        val legacyPersons = allPersons(AllPersonUseCase.I())
            .first()
            .map { it.person }
            .map { person ->
                Legacy.Person(
                    id = person.id,
                    name = person.name,
                    isFavourite = false,
                    notes = person.note
                )
            }

        val dreamPersonCrossrefs = allDreams
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

        try {
            Json.encodeToSink(root, zipStream.asSink().buffered())
        } catch (_: IOException) {
            return ExportResult.Failure.FileAccess(
                id = 0,
                started = startTimestamp,
                ended = Clock.System.now()
            )
        }

        zipStream.closeEntry()

        return null
    }
}
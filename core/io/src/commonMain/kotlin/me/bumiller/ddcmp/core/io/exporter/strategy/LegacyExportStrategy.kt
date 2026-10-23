package me.bumiller.ddcmp.core.io.exporter.strategy

import kotlinx.coroutines.flow.first
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.io.IOException
import kotlinx.io.buffered
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.io.encodeToSink
import me.bumiller.ddcmp.common.captureException
import me.bumiller.ddcmp.core.io.exporter.ZipFileExporter.DataExportStrategy
import me.bumiller.ddcmp.core.io.model.Legacy
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import no.synth.kmpzip.kotlinx.asSink
import no.synth.kmpzip.zip.ZipEntry
import no.synth.kmpzip.zip.ZipOutputStream

private const val FILE_NAME = "data.json"

/**
 * Backup Exporter implementation
 */
internal class LegacyExportStrategy(
    private val allDreams: AllDreamUseCase,
    private val allPersons: AllPersonUseCase
) : DataExportStrategy {

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeDataInto(zipStream: ZipOutputStream): DataExportStrategy.DataResult {
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
        } catch (e: IOException) {
            captureException(e)
            return DataExportStrategy.DataResult.WriteError(e)
        }

        return DataExportStrategy.DataResult.Success
    }
}
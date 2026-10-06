package me.bumiller.ddcmp.core.io.importer

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readString
import io.github.vinceglb.filekit.source
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.io.buffered
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.io.decodeFromSource
import me.bumiller.ddcmp.core.io.model.Legacy
import me.bumiller.ddcmp.domain.io.BackupImporter
import me.bumiller.ddcmp.domain.model.Confidentiality
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.model.ImportResult
import me.bumiller.ddcmp.domain.model.Person
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase
import me.bumiller.ddcmp.domain.usecase.person.SavePersonUseCase
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class LegacyImporter(
    private val timeZone: TimeZone,
    private val transferFavorites: Boolean,
    private val defaultConfidentiality: Confidentiality,
    private val createDream: SaveDreamUseCase,
    private val createPerson: SavePersonUseCase
) : BackupImporter {

    @OptIn(ExperimentalTime::class, ExperimentalSerializationApi::class)
    override suspend fun importBackup(file: PlatformFile): ImportResult {
        val now = Clock.System.now()

        val root = try {
            Json.decodeFromSource<Legacy.Root>(file.source().buffered())
        } catch (_: Exception) {
            return ImportResult.Failure.FileFormat(0, now)
        }

        // Check data integrity
        root.dreamPersonCrossref.forEach { crossref ->
            val dreamId = crossref.first
            val personId = crossref.second

            val dreamExists = root.dreams.any { it.id == dreamId }
            val personExists = root.persons.any { it.id == personId }

            if (!dreamExists || !personExists)
                return ImportResult.Failure.DataConsistency(0, now)
        }

        val dreams = root.dreams
            .map { legacyDream ->
                Dream(
                    id = legacyDream.id,
                    created = LocalDate.parse(legacyDream.created)
                        .atStartOfDayIn(timeZone),
                    edited = LocalDate.parse(legacyDream.updated)
                        .atStartOfDayIn(timeZone),
                    dreamt = LocalDate.parse(legacyDream.created)
                        .atStartOfDayIn(timeZone),
                    title = null,
                    description = legacyDream.description,
                    note = legacyDream.note,
                    favorite = transferFavorites && legacyDream.isFavourite,
                    confidentiality = defaultConfidentiality
                )
            }

        val persons = root.persons
            .map { legacyPerson ->
                Person(
                    id = legacyPerson.id,
                    created = now,
                    edited = now,
                    name = legacyPerson.name,
                    note = legacyPerson.notes
                )
            }

        persons.forEach { person ->
            createPerson(person)
        }

        dreams.forEach { dream ->
            val personsForDream = root.dreamPersonCrossref
                .filter { it.first == dream.id }
                .map { it.second }
                .mapNotNull { personId -> persons.find { it.id == personId } }

            val input = SaveDreamUseCase.I(
                dream = dream,
                persons = personsForDream,
                emotions = emptyList()
            )
            createDream(input)
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
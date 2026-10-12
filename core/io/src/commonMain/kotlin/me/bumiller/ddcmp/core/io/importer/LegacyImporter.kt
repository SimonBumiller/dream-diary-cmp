package me.bumiller.ddcmp.core.io.importer

import io.github.vinceglb.filekit.PlatformFile
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
import me.bumiller.ddcmp.domain.model.dream.Confidentiality
import me.bumiller.ddcmp.domain.model.dream.Dream
import me.bumiller.ddcmp.domain.model.io.ImportResult
import me.bumiller.ddcmp.domain.model.dream.Person
import me.bumiller.ddcmp.domain.model.dream.Tag
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase
import me.bumiller.ddcmp.domain.usecase.person.SavePersonUseCase
import me.bumiller.ddcmp.domain.usecase.tag.SaveTagUseCase
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class LegacyImporter(
    private val timeZone: TimeZone,
    private val transferFavorites: Boolean,
    private val transferRelations: Boolean,
    private val defaultConfidentiality: Confidentiality,
    private val createDream: SaveDreamUseCase,
    private val createPerson: SavePersonUseCase,
    private val createTag: SaveTagUseCase
) : BackupImporter {

    @OptIn(ExperimentalTime::class, ExperimentalSerializationApi::class)
    override suspend fun importBackup(file: PlatformFile): ImportResult {
        val startedTimestamp = Clock.System.now()

        val root = try {
            Json.decodeFromSource<Legacy.Root>(file.source().buffered())
        } catch (_: Exception) {
            return ImportResult.Failure.FileFormat(0, startedTimestamp, Clock.System.now())
        }

        // Check data integrity
        root.dreamPersonCrossref.forEach { crossref ->
            val dreamId = crossref.first
            val personId = crossref.second

            val dreamExists = root.dreams.any { it.id == dreamId }
            val personExists = root.persons.any { it.id == personId }

            if (!dreamExists || !personExists)
                return ImportResult.Failure.DataConsistency(0, startedTimestamp, Clock.System.now())
        }
        root.personRelationCrossrefs.forEach { crossref ->
            val personId = crossref.first
            val relationId = crossref.second

            val personExists = root.persons.any { it.id == personId }
            val relationExists = root.relations.any { it.id == relationId }

            if (!relationExists || !personExists)
                return ImportResult.Failure.DataConsistency(0, startedTimestamp, Clock.System.now())
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

        val tagsFromRelations = root.relations
            .map { relation ->
                Tag(
                    id = relation.id,
                    created = Clock.System.now(),
                    edited = Clock.System.now(),
                    name = relation.name,
                    description = relation.notes.ifBlank { null },
                    color = relation.color
                )
            }

        val persons = root.persons
            .map { legacyPerson ->
                Person(
                    id = legacyPerson.id,
                    created = startedTimestamp,
                    edited = startedTimestamp,
                    name = legacyPerson.name,
                    note = legacyPerson.notes
                )
            }

        if(transferRelations) {
            tagsFromRelations.forEach { tag ->
                val input = SaveTagUseCase.I(tag)
                createTag(input)
            }
        }

        persons.forEach { person ->
            val tags = root.personRelationCrossrefs
                .filter { it.first == person.id }
                .map { it.second }
                .map { tagsFromRelations.find { tag -> tag.id == it }!! }
                .let { if(transferRelations) it else emptyList() }

            val input = SavePersonUseCase.I(person, tags)
            createPerson(input)
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
            started = startedTimestamp,
            ended = Clock.System.now()
        )

        return result
    }

}
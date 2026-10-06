@file:OptIn(ExperimentalTime::class)

package me.bumiller.ddcmp.core.io.normal

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.cacheDir
import io.github.vinceglb.filekit.createDirectories
import io.github.vinceglb.filekit.path
import io.mockk.Awaits
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import me.bumiller.ddcmp.core.io.exporter.ListBasedExporter
import me.bumiller.ddcmp.core.io.importer.ListBasedImporter
import me.bumiller.ddcmp.domain.model.Arousal
import me.bumiller.ddcmp.domain.model.Confidentiality
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.model.Emotion
import me.bumiller.ddcmp.domain.model.ExportResult
import me.bumiller.ddcmp.domain.model.IOFileType
import me.bumiller.ddcmp.domain.model.Person
import me.bumiller.ddcmp.domain.model.Valence
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.AllEmotionsUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.SaveEmotionUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.domain.usecase.person.SavePersonUseCase
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

val now = Clock.System.now()
val tomorrow = now.plus(1.days)
val yesterday = now.minus(1.days)

val dreams = listOf(
    Dream(1, now, now, yesterday, "title", "desc", null, false, Confidentiality.Medium),
    Dream(
        2,
        now,
        yesterday,
        yesterday,
        "title 2",
        "sdfsdff lorem ipsum",
        "hello note",
        true,
        Confidentiality.None
    ),
)

val persons = listOf(
    Person(1, yesterday, now, "Jane Doe", null),
    Person(2, now, now, "John Pork", "He is very smart"),
    Person(3, now, tomorrow, "Niklas Bumiller", null),
)

val emotions = listOf(
    Emotion(1, yesterday, now, Arousal(0.4F), Valence(0.7F), "Jealousy"),
    Emotion(2, now, now, Arousal(-0.4F), Valence(0.9F), "Happiness"),
    Emotion(3, tomorrow, tomorrow, Arousal(-0.99F), Valence(0.01F), "Surprise")
)

val outputs = listOf(
    AllDreamUseCase.O(dreams[0], listOf(emotions[2]), listOf(persons[1], persons[2])),
    AllDreamUseCase.O(dreams[1], listOf(emotions[0], emotions[1]), listOf(persons[0])),
)

class NormalExporterTest {

    lateinit var allDreams: AllDreamUseCase
    lateinit var allEmotions: AllEmotionsUseCase
    lateinit var allPersons: AllPersonUseCase

    @BeforeTest
    fun setup() {
        FileKit.init("me.bumiller.ddcmp.test")

        allDreams = mockk()
        every { allDreams(any()) } returns flowOf(outputs)

        allEmotions = mockk()
        every { allEmotions(any()) } returns flowOf(emotions)

        allPersons = mockk()
        every { allPersons(any()) } returns flowOf(persons)
    }

    @Test
    fun `ListBasedExporter works`() = runBlocking {
        val subject = ListBasedExporter(allDreams, allPersons, allEmotions, IOFileType.Json)

        val path = "/home/simon/AndroidStudioProjects/dream-diary-cmp/temp/export.zip"

        val file = PlatformFile(path)

        // Export data
        subject.export(file)

        println("Exported file to: ${file.path}")

        val saveDream = mockk<SaveDreamUseCase>()
        val savedDreams = mutableListOf<SaveDreamUseCase.I>()
        coEvery { saveDream.invoke(capture(savedDreams)) } returns dreams.first()

        val savePerson = mockk<SavePersonUseCase>()
        val savedPersons = mutableListOf<Person>()
        coEvery { savePerson.invoke(capture(savedPersons)) } returns persons.first()

        val saveEmotion = mockk<SaveEmotionUseCase>()
        val savedEmotions = mutableListOf<Emotion>()
        coEvery { saveEmotion.invoke(capture(savedEmotions)) } returns emotions.first()

        val importer = ListBasedImporter(saveDream, savePerson, saveEmotion, IOFileType.Json)

        importer.importBackup(file)

        println("Imported from file: ${file.path}")
        println("Got saved dreams: $savedDreams")
        println("Got saved persons: $savedPersons")
        println("Got saved emotions: $savedEmotions")
    }

}
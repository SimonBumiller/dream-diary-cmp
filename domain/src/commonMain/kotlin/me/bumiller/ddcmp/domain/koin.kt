package me.bumiller.ddcmp.domain

import me.bumiller.ddcmp.domain.stats.dream.global.DreamLengthFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.dream.global.DreamPersonCountFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.dream.global.DreamStreakStatistic
import me.bumiller.ddcmp.domain.stats.dream.global.DreamTimeFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.dream.global.DreamWeekdayFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.dream.global.DreamWeeklyFrequencyStatistic
import me.bumiller.ddcmp.domain.stats.dream.global.MarkedDreamLengthFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.dream.global.MarkedDreamPersonCountFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.person.PersonInfluencingEmotionsStatistic
import me.bumiller.ddcmp.domain.stats.person.PersonInfluencingPersonsStatistic
import me.bumiller.ddcmp.domain.stats.person.PersonOccurrenceStatistic
import me.bumiller.ddcmp.domain.stats.person.PersonsOccurrencesCountStatistic
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.dream.DreamDatesUseCase
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.AllEmotionsUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.SaveEmotionUseCase
import me.bumiller.ddcmp.domain.usecase.io.GetImportLogUsecase
import me.bumiller.ddcmp.domain.usecase.io.ImportUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.domain.usecase.person.SavePersonUseCase
import me.bumiller.ddcmp.domain.usecase.settings.GetSettingsUseCase
import me.bumiller.ddcmp.domain.usecase.settings.SetSettingsUseCase
import org.koin.dsl.module

/**
 * The Koin module for the domain component
 */
val domain = module {
    single { AllDreamUseCase(get(), get(), get()) }
    single { DreamDatesUseCase(get()) }
    single { SaveDreamUseCase(get(), get(), get()) }

    single { AllEmotionsUseCase(get()) }
    single { SaveEmotionUseCase(get()) }

    single { AllPersonUseCase(get()) }
    single { SavePersonUseCase(get()) }

    single { GetSettingsUseCase(get()) }
    single { SetSettingsUseCase(get()) }

    single { ImportUseCase(get(), get(), get(), get(), get(), get()) }
    single { GetImportLogUsecase(get()) }


    single { DreamTimeFrequencyHistogram(get()) }
    single { DreamWeekdayFrequencyHistogram(get()) }
    single { DreamStreakStatistic(get()) }
    single { DreamWeeklyFrequencyStatistic(get()) }
    single { DreamPersonCountFrequencyHistogram(get(), get()) }
    single { MarkedDreamPersonCountFrequencyHistogram(get(), get()) }
    single { DreamLengthFrequencyHistogram(get()) }
    single { MarkedDreamLengthFrequencyHistogram(get(), get()) }
    single { PersonInfluencingEmotionsStatistic(get()) }
    single { PersonInfluencingPersonsStatistic(get()) }
    single { PersonOccurrenceStatistic(get()) }
    single { PersonsOccurrencesCountStatistic(get()) }
}
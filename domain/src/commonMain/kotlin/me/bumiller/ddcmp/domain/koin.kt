package me.bumiller.ddcmp.domain

import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.SaveEmotionUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.AllEmotionsUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.domain.usecase.settings.GetSettingsUseCase
import me.bumiller.ddcmp.domain.usecase.settings.SetSettingsUseCase
import org.koin.dsl.module

/**
 * The Koin module for the domain component
 */
val domain = module {
    single { AllDreamUseCase(get(), get(), get()) }
    single { SaveDreamUseCase(get(), get(), get()) }

    single { AllEmotionsUseCase(get(), get()) }
    single { SaveEmotionUseCase(get()) }

    single { AllPersonUseCase(get(), get()) }

    single { GetSettingsUseCase(get()) }
    single { SetSettingsUseCase(get()) }
}
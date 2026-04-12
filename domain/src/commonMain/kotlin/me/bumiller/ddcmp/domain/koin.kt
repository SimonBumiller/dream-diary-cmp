package me.bumiller.ddcmp.domain

import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.AllEmotionsUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.domain.usecase.settings.GetSettingsUseCase
import me.bumiller.ddcmp.domain.usecase.settings.SetSettingsUseCase
import org.koin.dsl.module

/**
 * The Koin module for the domain component
 */
val domain = module {
    single { AllDreamUseCase(get()) }

    single { AllEmotionsUseCase(get(), get()) }

    single { AllPersonUseCase(get(), get()) }

    single { GetSettingsUseCase(get()) }
    single { SetSettingsUseCase(get()) }
}
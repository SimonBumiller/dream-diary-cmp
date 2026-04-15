package me.ddcmp.feature.onboarding.di

import me.ddcmp.feature.onboarding.entries.functional.FunctionalOnboardingViewmodel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * The koin module for the onboarding section.
 */
val onboarding = module {
    viewModelOf(::FunctionalOnboardingViewmodel)
}
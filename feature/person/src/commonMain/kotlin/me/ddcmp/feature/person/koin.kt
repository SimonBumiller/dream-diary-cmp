package me.ddcmp.feature.person

import me.ddcmp.feature.person.entries.personadd.PersonAddViewModel
import me.ddcmp.feature.person.entries.persondetail.PersonDetailViewModel
import me.ddcmp.feature.person.entries.personlist.PersonListViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * The koin module for the person module.
 */
public val person: Module = module {
    factoryOf(::PersonListViewModel)
    factoryOf(::PersonAddViewModel)
    factoryOf(::PersonDetailViewModel)
}
package me.bumiller.ddcmp.work

import org.koin.core.module.Module
import org.koin.dsl.module

expect fun Module.provideWorkers()

val work = module {
    provideWorkers()
}
package me.bumiller.ddcmp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent

class AndroidApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@AndroidApplication)
        }
    }

}
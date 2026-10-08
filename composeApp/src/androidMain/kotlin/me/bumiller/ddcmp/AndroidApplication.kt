package me.bumiller.ddcmp

import android.app.Application
import dev.brewkits.kmpworkmanager.KmpWorkManager
import me.bumiller.ddcmp.work.factory.AndroidWorkerFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext

class AndroidApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@AndroidApplication)
        }

        KmpWorkManager.initialize(
            context = this,
            workerFactory = AndroidWorkerFactory(GlobalContext.get())
        )
    }

}
package com.aditya1875.baskit

import android.app.Application
import com.aditya1875.baskit.di.preferencesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(
                preferencesModule
            )
        }
    }
}
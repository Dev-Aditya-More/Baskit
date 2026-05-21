package com.aditya1875.baskit

import android.app.Application
import com.aditya1875.baskit.history.presentation.di.databaseModule
import com.aditya1875.baskit.history.presentation.di.historyModule
import com.aditya1875.baskit.insights.di.insightsModule
import com.aditya1875.baskit.product.di.productModule
import com.aditya1875.baskit.profile.di.authModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(productModule, historyModule, databaseModule, authModule, insightsModule)
        }
    }
}

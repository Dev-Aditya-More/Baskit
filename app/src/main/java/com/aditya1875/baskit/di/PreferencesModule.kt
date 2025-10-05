package com.aditya1875.baskit.di

import com.aditya1875.baskit.utils.PreferencesManager
import org.koin.dsl.module

val preferencesModule = module {
    single { PreferencesManager(get()) }
}
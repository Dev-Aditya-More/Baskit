package com.aditya1875.baskit.history.presentation.di

import com.aditya1875.baskit.history.data.local.AppDatabase
import com.aditya1875.baskit.history.data.repository.ScanHistoryRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { AppDatabase.getInstance(androidContext()) }
    single { get<AppDatabase>().scanDao() }
    single { ScanHistoryRepository(get()) }
}
package com.aditya1875.baskit.history.presentation.di

import com.aditya1875.baskit.history.presentation.viewmodel.ScanHistoryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val historyModule = module {
    viewModel { ScanHistoryViewModel(get()) }
}
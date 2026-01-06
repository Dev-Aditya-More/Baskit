package com.aditya1875.baskit.di

import com.aditya1875.baskit.core.data.repository.ProductRepository
import com.aditya1875.baskit.ui.viewmodel.ProductViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { ProductRepository() }
    viewModel { ProductViewModel(get()) }
}
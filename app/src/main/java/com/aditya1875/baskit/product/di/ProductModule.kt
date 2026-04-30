package com.aditya1875.baskit.product.di

import com.aditya1875.baskit.product.data.repository.ProductRepository
import com.aditya1875.baskit.ui.viewmodel.ProductViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val productModule = module {

    single { ProductRepository() }
    viewModel { ProductViewModel(get()) }
}
package com.example.ecom.di

import com.example.ecom.network.ApiClient
import com.example.ecom.network.ApiManager
import com.example.ecom.network.getApiManager
import com.example.ecom.pages.productDetailsPage.repository.ProductDetailsRepository
import com.example.ecom.pages.productDetailsPage.viewmodel.ProductDetailsViewModel
import com.example.ecom.pages.productListPage.repository.ProductListRepository
import com.example.ecom.pages.productListPage.viewmodel.ProductListViewModel
import com.example.ecom.pages.userProfile.viewmodel.UserProfileViewModel
import com.example.ecom.utils.CommonUtils
import com.example.ecom.utils.isNetworkConnected
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val networkModule = module {
    singleOf(::ApiClient)
    factory(named("IsNetworkConnected")) { isNetworkConnected(androidContext()) }
    factory { (url: String) -> getApiManager(get(), url) }
    singleOf(::ApiManager)
    singleOf(::CommonUtils)
}

private val repositories = module {
    singleOf(::ProductDetailsRepository)
    singleOf(::ProductListRepository)
}

private val viewModels = module {
    viewModelOf(::ProductListViewModel)
    viewModelOf(::ProductDetailsViewModel)
    viewModelOf(::UserProfileViewModel)
}

val appModules = listOf(networkModule, viewModels, repositories)


inline fun <reified T> getKoinInstance(name: String): T {
    return object : KoinComponent {
        val value: T by inject(named(name))
    }.value
}
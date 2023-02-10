package com.example.ecom.network

import com.example.ecom.utils.makeCall
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

fun getApiManager(apiClient: ApiClient, url: String): AppApiInterface {
    return apiClient.getClient(url).create(AppApiInterface::class.java)
}

class ApiManager : KoinComponent {
    private val appApiInterface: AppApiInterface by inject {
        parametersOf("https://run.mocky.io")
    }

    suspend fun getProductList(code: Int = ApiConstants.PRODUCT_LIST) = makeCall(code) {
        appApiInterface.getProductList()
    }

    suspend fun getSimilarProductList(code: Int = ApiConstants.SIMILAR_PRODUCT_LIST) = makeCall(code) {
        appApiInterface.getSimilarProductList()
    }

    suspend fun getBrandList(code: Int = ApiConstants.BRAND_LIST) = makeCall(code) {
        appApiInterface.getBrands()
    }

    suspend fun getBannerList(code: Int = ApiConstants.BANNER_LIST) = makeCall(code) {
        appApiInterface.getBanners()
    }

    suspend fun getProductVariants(code: Int = ApiConstants.PRODUCT_VARIANTS) = makeCall(code) {
        appApiInterface.getProductVariants()
    }

    suspend fun getUserProfile(code: Int = ApiConstants.USER_PROFILE) = makeCall(code) {
        appApiInterface.getUserProfile()
    }
}
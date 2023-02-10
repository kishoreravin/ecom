package com.example.ecom.pages.productDetailsPage.repository

import com.example.ecom.models.base.ApiState
import com.example.ecom.network.ApiManager
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductDetailsRepository(private val apiManager: ApiManager) {

    suspend fun getProductDetails(productCode: String) = channelFlow {
        apiManager.getProductList().collectLatest {
            send(it)
            if (it is ApiState.Success) {
                launch {
                    apiManager.getProductVariants().collect(::send)
                }
                launch {
                    apiManager.getSimilarProductList().collect(::send)
                }
            }
        }

    }
}
package com.example.ecom.pages.productListPage.repository

import com.example.ecom.models.base.ApiState
import com.example.ecom.network.ApiManager
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductListRepository(private val apiManager: ApiManager) {

    suspend fun getProductListPageRelatedData() = channelFlow {
        apiManager.getProductList().collectLatest {
            send(it)
            if(it is ApiState.Success) {
                launch {
                    apiManager.getBrandList().collect(::send)
                }
                launch {
                    apiManager.getBannerList().collect(::send)
                }
            }
        }
    }

    suspend fun addMoreProducts() = apiManager.getProductList()
}
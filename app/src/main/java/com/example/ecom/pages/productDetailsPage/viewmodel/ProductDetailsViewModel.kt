package com.example.ecom.pages.productDetailsPage.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ecom.base.BaseViewModel
import com.example.ecom.models.ProductModel
import com.example.ecom.models.base.ApiState
import com.example.ecom.network.ApiConstants
import com.example.ecom.pages.productDetailsPage.repository.ProductDetailsRepository
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProductDetailsViewModel(private val productDetailsRepository: ProductDetailsRepository) : BaseViewModel() {


    lateinit var productCode: String
    val productDetailsLiveData = MutableLiveData<ProductModel>()
    val similarProductsLiveData = MutableLiveData<List<ProductModel>?>()
    val variantLiveData = MutableLiveData<List<ProductModel>?>()

    fun getProductDetails(productCode: String) {
        this.productCode = productCode
        viewModelScope.launch {
            productDetailsRepository.getProductDetails(productCode)
                .onStart { showLoader() }
                .onCompletion { dismissLoader() }
                .collect {
                    when (it) {
                        is ApiState.Error -> showApiError()
                        is ApiState.Failed -> showApiError()
                        ApiState.NoNetwork -> showNetworkError()
                        is ApiState.Success -> {
                            when (it.id) {
                                ApiConstants.PRODUCT_LIST -> {
                                    productDetailsLiveData.value = it.result.result.first { product -> product.id == productCode }
                                }
                                ApiConstants.SIMILAR_PRODUCT_LIST -> similarProductsLiveData.value = it.result.result
                                ApiConstants.PRODUCT_VARIANTS -> variantLiveData.value = it.result.result
                            }
                        }
                    }
                }
        }
    }

    override fun onRetryClickListener() {
        if (this::productCode.isInitialized) {
            getProductDetails(productCode)
        }
    }
}
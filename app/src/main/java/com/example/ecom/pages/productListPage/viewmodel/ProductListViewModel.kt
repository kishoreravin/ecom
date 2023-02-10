package com.example.ecom.pages.productListPage.viewmodel

import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecom.base.BaseViewModel
import com.example.ecom.models.BannerModel
import com.example.ecom.models.BrandModel
import com.example.ecom.models.ProductModel
import com.example.ecom.models.base.ApiState
import com.example.ecom.network.ApiConstants
import com.example.ecom.pages.productListPage.repository.ProductListRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProductListViewModel(private val productListRepository: ProductListRepository) : BaseViewModel() {

    val productListLiveData = MutableLiveData<List<ProductModel>>()
    val brandListLiveData = MutableLiveData<List<BrandModel>>()
    val bannerListLiveData = MutableLiveData<List<BannerModel>>()
    val searchResult = MutableLiveData<List<ProductModel>>()
    val productList = mutableListOf<ProductModel>()
    var pageIndex = 1
    var isUserSearching = false

    fun getProductList() {
        viewModelScope.launch {
            productListRepository.getProductListPageRelatedData()
                .onStart { showLoader() }
                .onCompletion { dismissLoader() }
                .collectLatest {
                    when (it) {
                        is ApiState.Error -> {
                            Log.e("Error", it.exception.toString())
                        }
                        is ApiState.Failed -> showApiError()
                        ApiState.NoNetwork -> showNetworkError()
                        is ApiState.Success -> {
                            when (it.id) {
                                ApiConstants.PRODUCT_LIST -> {
                                    productList.addAll(it.result.result.map { model -> model as ProductModel }.toMutableList())
                                    productListLiveData.value = productList
                                }
                                ApiConstants.BRAND_LIST -> brandListLiveData.value = it.result.result.map { model -> model as BrandModel }
                                ApiConstants.BANNER_LIST -> bannerListLiveData.value = it.result.result.map { model -> model as BannerModel }
                            }
                        }
                    }
                }
        }
    }

    fun getMoreProducts() {
        viewModelScope.launch {
            productListRepository.addMoreProducts()
                .onStart { showLoader() }
                .onCompletion { dismissLoader() }
                .collectLatest {
                    when (it) {
                        is ApiState.Error -> {}
                        is ApiState.Failed -> {}
                        ApiState.NoNetwork -> showNetworkError()
                        is ApiState.Success -> {
                            it.result.result.forEach { e -> e.id = ((e.id?.toIntOrNull() ?: 1) + (pageIndex * 10)).toString() }
                            productList.addAll(it.result.result)
                            productListLiveData.value = it.result.result!!
                        }
                    }
                }
        }
    }

    fun onPageScroll(layoutManager: GridLayoutManager): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.findLastCompletelyVisibleItemPosition() + 1
                if (visibleItemCount == layoutManager.itemCount && !isUserSearching) {
                    getMoreProducts()
                }
            }
        }
    }

    override fun onRetryClickListener() {
        getProductList()
    }

    fun onQueryTextListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                isUserSearching = true
                if (newText.isEmpty()) {
                    searchResult.value = productList
                    isUserSearching = false
                    return true
                }
                searchResult.value = productListLiveData.value!!.filter { it.name?.contains(newText, true) ?: false }
                isUserSearching = false
                return true
            }
        }
    }
}
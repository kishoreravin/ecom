package com.example.ecom.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    val loaderLiveData = MutableLiveData<Boolean>()
    val showNetworkError = MutableLiveData<Boolean>()
    val showWebApiError = MutableLiveData<Boolean>()

    fun showApiError() {
        showWebApiError.value = true
    }

    fun hideApiError() {
        showWebApiError.value = false
    }

    fun showNetworkError() {
        showNetworkError.value = true
    }

    fun hideNetworkError() {
        showNetworkError.value = false
    }

    fun showLoader() {
        loaderLiveData.value = true
    }

    fun dismissLoader() {
        loaderLiveData.value = false
    }

    abstract fun onRetryClickListener()
}
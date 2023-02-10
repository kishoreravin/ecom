package com.example.ecom.models.base

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("status") val status: Boolean,
    @SerializedName("list") val result: T
) {

    fun isResultAvailable() = result != null
}
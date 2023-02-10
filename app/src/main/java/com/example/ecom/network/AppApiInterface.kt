package com.example.ecom.network

import com.example.ecom.models.BannerModel
import com.example.ecom.models.BrandModel
import com.example.ecom.models.ProductModel
import com.example.ecom.models.UserModel
import com.example.ecom.models.base.BaseResponse
import retrofit2.http.GET

object ApiConstants {
    const val GET_PRODUCT_LIST = "/v3/79dd309f-d1f2-4477-978d-3088f1c19aa4"
    const val GET_BRAND_LIST = "/v3/5c3532d2-62a8-4a21-8fdb-311806489236"
    const val GET_BANNER_LIST = "/v3/7556f448-2a7b-4593-bcfc-b8371a7d5848"
    const val GET_PRODUCT_VARIANTS = "/v3/b141af7d-b551-45ac-a411-57e195777710"
    const val GET_USER_PROFILE = "/v3/ee117a49-f57d-414f-8060-d6f2489828d8"

    const val PRODUCT_LIST = 1000
    const val SIMILAR_PRODUCT_LIST = 1001
    const val BRAND_LIST = 1002
    const val BANNER_LIST = 1003
    const val PRODUCT_VARIANTS = 1004
    const val USER_PROFILE = 1005
}

interface AppApiInterface {

    @GET(ApiConstants.GET_PRODUCT_LIST)
    suspend fun getProductList(): BaseResponse<List<ProductModel>>

    @GET(ApiConstants.GET_PRODUCT_LIST)
    suspend fun getSimilarProductList(): BaseResponse<List<ProductModel>>

    @GET(ApiConstants.GET_PRODUCT_VARIANTS)
    suspend fun getProductVariants(): BaseResponse<List<ProductModel>>

    @GET(ApiConstants.GET_BRAND_LIST)
    suspend fun getBrands(): BaseResponse<List<BrandModel>>

    @GET(ApiConstants.GET_BANNER_LIST)
    suspend fun getBanners(): BaseResponse<List<BannerModel>>

    @GET(ApiConstants.GET_USER_PROFILE)
    suspend fun getUserProfile(): BaseResponse<UserModel>
}
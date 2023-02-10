package com.example.ecom.models

import com.google.gson.annotations.SerializedName
import kotlin.random.Random

enum class ViewTypes {
    Product, Banner, None
}

data class ProductModel(
    @SerializedName("id", alternate = ["variantProductCode"]) var id: String? = null,
    @SerializedName("brand") val brand: String? = null,
    @SerializedName("name", alternate = ["variantName"]) val name: String? = null,
    @SerializedName("productDesc") val productDesc: String? = null,
    @SerializedName("price") val price: String? = null,
    @SerializedName("offerPrice") val offerPrice: String? = null,
    @SerializedName("productUrl") var productUrl: String? = null,
    var type: ViewTypes = ViewTypes.None,
    var brands: List<BrandModel> = emptyList(),
    var banners: List<BannerModel> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductModel
        return false
    }

    override fun hashCode(): Int {
        return Random(100).nextInt()
    }
}

data class BrandModel(
    @SerializedName("brandName") val brandName: String? = null,
    @SerializedName("brandUrl") val brandUrl: String? = null
)

data class BannerModel(
    @SerializedName("bannerName") val bannerName: String? = null,
    @SerializedName("bannerUrl") val bannerUrl: String? = null
)
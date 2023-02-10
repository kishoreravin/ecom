package com.example.ecom.models

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("id") val id: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("firstname") val firstname: String? = null,
    @SerializedName("lastName") val lastName: String? = null,
    @SerializedName("dob") val dob: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("pointsEarned") val pointsEarned: String? = null,
    @SerializedName("walletBalance") val walletBalance: String? = null,
)
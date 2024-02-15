package com.example.kktext.Model


import com.google.gson.annotations.SerializedName

data class UpdateClientBean(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // client Update successfully.
) {
    data class Data(
        @SerializedName("active")
        val active: Int, // 1
        @SerializedName("address")
        val address: String, // xvx
        @SerializedName("city")
        val city: String, // Mohali
        @SerializedName("created_At")
        val createdAt: String, // 2023-09-3014:00:20
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("name")
        val name: String, // xcv
        @SerializedName("number")
        val number: String, // 9872085586
        @SerializedName("state")
        val state: String, // Punjab
        @SerializedName("updated_at")
        val updatedAt: String // 2024-02-08 16:54:12
    )
}
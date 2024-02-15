package com.example.kktext.Model


import com.google.gson.annotations.SerializedName

data class OfficeTeamBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("active")
        val active: Int, // 1
        @SerializedName("created_at")
        val createdAt: String, // 2023-09-26 12:15:26
        @SerializedName("department")
        val department: String, // 2
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("mobile")
        val mobile: String, // 7888491677
        @SerializedName("name")
        val name: String, // Mrs. Bhawna Arora 
        @SerializedName("updated_at")
        val updatedAt: String // 2024-01-06 12:04:24
    )
}
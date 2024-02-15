package com.example.kktext.Model


import com.google.gson.annotations.SerializedName

data class MEPBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("address")
        val address: String, // asdf
        @SerializedName("city")
        val city: String, // Chandigarh
        @SerializedName("created_at")
        val createdAt: String, // 2024-02-0817:50:12
        @SerializedName("doa")
        val doa: String, // 2024-02-16
        @SerializedName("dob")
        val dob: String, // 2024-02-01
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("name")
        val name: String, // update testmep
        @SerializedName("name2")
        val name2: String, // update test mep (8596858596)
        @SerializedName("number")
        val number: String, // 8596858596
        @SerializedName("state")
        val state: String // Chandigarh
    )
}
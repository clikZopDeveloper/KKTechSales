package com.example.kktext.Model


import com.google.gson.annotations.SerializedName

data class LeadProductListBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("created_at")
        val createdAt: String, // 2024-02-1420:30:30
        @SerializedName("id")
        val id: Int, // 4669
        @SerializedName("is_delivered")
        val isDelivered: Int, // 0
        @SerializedName("lead_id")
        val leadId: Int, // 2552
        @SerializedName("product_id")
        val productId: Int, // 1
        @SerializedName("product_name")
        val productName: String, // VORDOSCH KRISTALL KLAR STRAINER VALUE1-1/4"
        @SerializedName("product_price")
        val productPrice: String, // 4915.00
        @SerializedName("qty")
        val qty: String, // 3.00
        @SerializedName("updated_at")
        val updatedAt: Any // null
    )
}
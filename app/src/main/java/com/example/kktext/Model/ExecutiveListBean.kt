package com.example.kktext.Model


import com.google.gson.annotations.SerializedName

data class ExecutiveListBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("area_id")
        val areaId: Any, // null
        @SerializedName("created_at")
        val createdAt: String, // 2023-10-0213:38:50
        @SerializedName("email")
        val email: String, // rajvir@gmail.com
        @SerializedName("fcm_token")
        val fcmToken: Any, // null
        @SerializedName("firebase_token")
        val firebaseToken: String,
        @SerializedName("id")
        val id: Int, // 42
        @SerializedName("is_active")
        val isActive: Int, // 1
        @SerializedName("last_ip")
        val lastIp: Any, // null
        @SerializedName("last_location")
        val lastLocation: String, // 30.1216175,75.817078
        @SerializedName("last_login")
        val lastLogin: String, // 2024-01-2413:51:08
        @SerializedName("location_time")
        val locationTime: String, // 2024-01-2413:49:44
        @SerializedName("name")
        val name: String, // Mr. Rajvir
        @SerializedName("parent_id")
        val parentId: Int, // 37
        @SerializedName("password")
        val password: String, // rajvir@853
        @SerializedName("phone")
        val phone: String, // 7888814853
        @SerializedName("sm_id")
        val smId: Int, // 37
        @SerializedName("token")
        val token: String, // La2o4vMLalZW
        @SerializedName("updated_at")
        val updatedAt: String, // 2024-01-24 13:51:08
        @SerializedName("user_type")
        val userType: String // Sales executive
    )
}
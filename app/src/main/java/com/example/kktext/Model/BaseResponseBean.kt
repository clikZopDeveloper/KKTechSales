package com.example.kktext.Model


import com.google.gson.annotations.SerializedName

data class BaseResponseBean(

    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String ,
    @SerializedName("drawableId")
    val drawableId: Int
) {

}
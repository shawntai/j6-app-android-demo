package com.siemens.j6_app_android_demo.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Material(
    @SerializedName("id")
    var id: Int,
    @SerializedName("code")
    var code: String,
    @SerializedName("desc")
    var name: String,
    @SerializedName("qty")
    var qty: Int,
    @SerializedName("unit")
    var unit: String,
    @SerializedName("unitRate")
    var unitRate: Int,
    @SerializedName("price")
    var price: Int,
    @SerializedName("imageUrl")
    var imageUrl: String
): Serializable {
    var isSelected = false
}
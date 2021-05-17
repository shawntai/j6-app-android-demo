package com.siemens.j6_app_android_demo.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Employee(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("department")
    val department: String,
    @SerializedName("position")
    val position: String,
    @SerializedName("contact")
    val contact: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
): Serializable {
    var isSelected = false
}
package com.siemens.j6_app_android_demo.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Building(
    @SerializedName("address")
    val address: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
): Serializable
package com.siemens.j6_app_android_demo.models

import com.google.gson.annotations.SerializedName

data class Tenant(
    @SerializedName("contactPerson")
    val contactPerson: ContactPerson,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("unit")
    val unit: String
)
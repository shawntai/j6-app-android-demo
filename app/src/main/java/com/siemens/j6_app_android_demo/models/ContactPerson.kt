package com.siemens.j6_app_android_demo.models

import com.google.gson.annotations.SerializedName

data class ContactPerson(
    @SerializedName("contactPerson")
    val contact: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("name")
    val name: String
)
package com.siemens.j6_app_android_demo.models

import com.google.gson.annotations.SerializedName

data class IssuedBy(
        @SerializedName("email")
        val email: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("name")
        val name: String
)
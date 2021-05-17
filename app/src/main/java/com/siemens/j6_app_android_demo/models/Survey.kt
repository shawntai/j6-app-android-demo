package com.siemens.j6_app_android_demo.models

import com.google.gson.annotations.SerializedName

data class Survey(
        @SerializedName("item")
        val item: String,
        @SerializedName("score")
        var score: Int
)
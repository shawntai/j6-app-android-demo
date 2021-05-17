package com.siemens.j6_app_android_demo.models

import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("name")
       val name: String,
        @SerializedName("title")
       val title: String,
        @SerializedName("imgUrl")
       val imgUrl: String
)
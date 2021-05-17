package com.siemens.j6_app_android_demo.models

import com.google.gson.annotations.SerializedName

data class Completion(
        @SerializedName("remark")
        val remark: String?, // empty string
        @SerializedName("result")
        val result: String?  // empty string
)
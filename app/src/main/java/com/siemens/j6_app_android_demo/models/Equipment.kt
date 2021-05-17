package com.siemens.j6_app_android_demo.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Equipment(
        @SerializedName("eid")
        var eid: String,
        @SerializedName("desc")
        var description: String,
        @SerializedName("downtime")
        var downtime: String,
        @SerializedName("condition")
        var condition: String,
        @SerializedName("id")
        var id: Int,
        @SerializedName("imageUrl")
        var imageUrl: String
): Serializable
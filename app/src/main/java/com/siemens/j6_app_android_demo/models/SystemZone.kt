package com.siemens.j6_app_android_demo.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SystemZone(
//        @SerializedName("id")
//        val id: Int,
        @SerializedName("name")
        val name: String
): Serializable {
        var isSelected = false
}

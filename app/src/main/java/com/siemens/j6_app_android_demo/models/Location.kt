package com.siemens.j6_app_android_demo.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Location(
    @SerializedName("id")
    val id: Int,
    @SerializedName("building")
    val building: Building,
    @SerializedName("level")
    val level: String,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("room")
    val room: String
): Serializable {
    var isSelected = true
    fun getId(): String {
        return building.id + level + room
    }
    fun ready(): Boolean {
        return building.name != "null" && level != "null" && room != "null"
    }
}
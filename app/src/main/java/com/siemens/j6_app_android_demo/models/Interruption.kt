package com.siemens.j6_app_android_demo.models

import com.google.gson.annotations.SerializedName

data class Interruption(
        @SerializedName("remark")
        val remark: String,
        @SerializedName("systemZone")
        val systemZone: SystemZone,
        @SerializedName("severityLevel")
        val severityLevel: String?,
        @SerializedName("systemZoneFailureLevel")
        val systemZoneFailureLevel: String,
        @SerializedName("wholeSystemFailureLevel")
        val wholeSystemFailureLevel: String
    )
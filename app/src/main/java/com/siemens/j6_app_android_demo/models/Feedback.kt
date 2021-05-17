package com.siemens.j6_app_android_demo.models

import com.google.gson.annotations.SerializedName

data class Feedback(
        @SerializedName("comment")
        val comment: String?,
        @SerializedName("surveys")
        val surveys: List<Survey>,
        @SerializedName("attachments")
        val attachments: List<Attachment>?,
        @SerializedName("feedbackedAt")
        val feedbackedAt: String?
)
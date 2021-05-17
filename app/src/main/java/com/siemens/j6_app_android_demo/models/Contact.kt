package com.siemens.j6_app_android_demo.models

data class Contact(
        val tenant: String,
        val unit: String,
        val contactPerson: ContactPerson,
        val requestedAt: String,
)
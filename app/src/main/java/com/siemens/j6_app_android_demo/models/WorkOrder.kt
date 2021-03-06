package com.siemens.j6_app_android_demo.models

import com.google.gson.annotations.SerializedName

data class WorkOrder(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("workorderId")
        val workorderId: String?,
        @SerializedName("recordName")
        val recordName: String?,
        @SerializedName("status")
        val status: String?,
        @SerializedName("tenant")
        val tenant: Tenant?,
        @SerializedName("requestedAt")
        var requestedAt: String?,
        @SerializedName("category")
        val category: String?,
        @SerializedName("desc") //category
        val desc: String?,
        @SerializedName("remark") //category
        val remark: String?,
        @SerializedName("comment") // no comment
        val comment: String?,
        @SerializedName("plannedAt")
        var plannedAt: String?,
        @SerializedName("completedAt")
        var completedAt: String?,
        @SerializedName("issuedBy") // only use name for now
        val issuedBy: IssuedBy?,
        @SerializedName("issuedAt")
        var issuedAt: String?,
        @SerializedName("approvalComment") // "Approval comment"
        var approvalComment: String?,
//        @SerializedName("approvedBy")
//        val approvedBy: Employee?,
        @SerializedName("approvedAt")
        val approvedAt: String?,
        @SerializedName("locations")
        val locations: List<Location>?,
        @SerializedName("engineers")
        val engineers: List<Employee>?,
        @SerializedName("materials")
        val materials: List<Material>?,
        @SerializedName("equipments")
        val equipments: List<Equipment>?,
        @SerializedName("attachments")
        val attachments: List<Attachment>?,
        @SerializedName("interruption")
        val interruption: Interruption?,
        @SerializedName("completion")
        val completion: Completion?,
        @SerializedName("feedback")
        val feedback: Feedback?,
        @SerializedName("createdAt") // now?
        val createdAt: String?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
) {
        companion object {
                var ID = 10000
        }
}
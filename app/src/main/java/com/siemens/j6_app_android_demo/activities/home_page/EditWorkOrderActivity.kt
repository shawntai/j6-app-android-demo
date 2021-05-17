package com.siemens.j6_app_android_demo.activities.home_page

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.gson.Gson
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.models.WorkOrder

class EditWorkOrderActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_work_order)

        findViewById<TextView>(R.id.title).text = "Edit Work Order"

        val workOrder = Gson().fromJson(intent.getStringExtra("work_order"), WorkOrder::class.java)
        workOrder?.let {
            findViewById<TextView>(R.id.tenant).text = workOrder.tenant.name
            findViewById<TextView>(R.id.unit).text = workOrder.tenant.unit
            findViewById<TextView>(R.id.contact_person).text = workOrder.tenant.contactPerson.name
        }
    }
}
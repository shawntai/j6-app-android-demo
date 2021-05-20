package com.siemens.j6_app_android_demo.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.activities.home_page.AddNewWorkOrderActivity
import com.siemens.j6_app_android_demo.activities.home_page.EditWorkOrderActivity
import com.siemens.j6_app_android_demo.activities.home_page.HomePageActivity
import com.siemens.j6_app_android_demo.fragments.WorkOrderFragment
import com.siemens.j6_app_android_demo.models.MaintenanceDataModel
import com.siemens.j6_app_android_demo.models.WorkOrder
import com.siemens.j6_app_android_demo.models.WorkOrderAppointmentDataModel
import java.util.*

class NewWorkOrderAppointmentAdapter(private val context: Context, private val arrayList: ArrayList<WorkOrder>) : BaseAdapter() {
    private lateinit var title: TextView
    private lateinit var date: TextView
    private lateinit var place: TextView
    private lateinit var image: ImageView

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(
                R.layout.wo_appointment_list_item,
                parent,
                false
        )
        title = convertView.findViewById(R.id.appointment_title)
        date = convertView.findViewById(R.id.appointment_date)
        place = convertView.findViewById(R.id.appointment_place)
        image = convertView.findViewById(R.id.appointment_img)

        title.text = arrayList[position].recordName
        date.text = arrayList[position].plannedAt
        if (arrayList[position].locations.isNotEmpty()) {
            place.text = arrayList[position].locations[0].room + ", " + arrayList[position].locations[0].level + ", " + arrayList[position].locations[0].building.name
        }        //image.setImageResource(arrayList[position].img)
        //Glide.with(image.context).load(arrayList[position].imgUrl).fitCenter().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(image)
        convertView.setOnClickListener {
            val context = convertView.context as HomePageActivity
            context.editedWorkOrderPosition = position
            //val i = Intent(convertView.context, EditWorkOrderActivity::class.java)
            val i = Intent(context, AddNewWorkOrderActivity::class.java)
            i.putExtra("work_order", Gson().toJson(arrayList[position]))
            //convertView.context.startActivity(i)
            context.editWorkOrder.launch(i)
        }

        return convertView
    }

    private fun dateToString(date: Calendar): String {
        val strings = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        return strings[date.get(Calendar.MONTH)].substring(0, 3)+ " " + date.get(Calendar.DAY_OF_MONTH).toString() + ", " + date.get(Calendar.YEAR).toString()
    }
}
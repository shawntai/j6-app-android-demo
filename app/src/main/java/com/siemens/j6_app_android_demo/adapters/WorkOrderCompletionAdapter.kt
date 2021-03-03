package com.siemens.j6_app_android_demo.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.models.MaintenanceDataModel
import com.siemens.j6_app_android_demo.models.WorkOrderAppointmentDataModel
import com.siemens.j6_app_android_demo.models.WorkOrderCompletionDataModel
import java.util.*

class WorkOrderCompletionAdapter(private val context: Context, private val arrayList: java.util.ArrayList<WorkOrderCompletionDataModel>) : BaseAdapter() {
    private lateinit var title: TextView
    private lateinit var date: TextView
    private lateinit var place: TextView

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(
            R.layout.wo_completiont_list_item,
            parent,
            false
        )
        title = convertView.findViewById(R.id.completion_title)
        date = convertView.findViewById(R.id.completion_date)
        place = convertView.findViewById(R.id.completion_place)

        title.text = arrayList[position].title
        date.text = dateToString(arrayList[position].date)
        place.text = arrayList[position].place

        return convertView
    }

    private fun dateToString(date: Calendar): String {
        val strings = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        return strings[date.get(Calendar.MONTH)].substring(0, 3)+ " " + date.get(Calendar.DAY_OF_MONTH).toString() + ", " + date.get(Calendar.YEAR).toString()
    }
}
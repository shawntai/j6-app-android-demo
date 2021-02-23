package com.siemens.j6_app_android_demo

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class MaintenanceAdapter(private val context: Context, private val arrayList: java.util.ArrayList<MaintenanceDataModel>) : BaseAdapter() {
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

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(
            R.layout.maintenance_list_item,
            parent,
            false
        )
        title = convertView.findViewById(R.id.maintenance_title)
        date = convertView.findViewById(R.id.maintenance_date)
        place = convertView.findViewById(R.id.maintenance_place)
        image = convertView.findViewById(R.id.maintenance_img)

        title.text = arrayList[position].title
        date.text = dateToString(arrayList[position].date)
        place.text = arrayList[position].place
        image.setImageResource(arrayList[position].img)

        return convertView
    }

    private fun dateToString(date: Calendar): String {
        val strings = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        return strings[date.get(Calendar.MONTH)].substring(0, 3)+ " " + date.get(Calendar.DAY_OF_MONTH).toString() + ", " + date.get(Calendar.YEAR).toString()
    }
}
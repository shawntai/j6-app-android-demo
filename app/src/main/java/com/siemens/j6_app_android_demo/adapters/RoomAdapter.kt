package com.siemens.j6_app_android_demo.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.activities.home_page.AddNewLocationActivity
import com.siemens.j6_app_android_demo.models.Location

class RoomAdapter(
    private val context: Context,
    private val roomList: java.util.ArrayList<Location>
) : BaseAdapter() {

    private lateinit var roomName: TextView
    private lateinit var sepLine: View

    override fun getCount(): Int {
        return roomList.size
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
            R.layout.add_location_param_lv_item,
            parent,
            false
        )
        roomName = convertView.findViewById(R.id.option_name)
        sepLine = convertView.findViewById(R.id.sep_line)
        roomName.text = roomList[position].room
        if (position == roomList.size-1)
            sepLine.visibility = View.GONE
        convertView.setOnClickListener {
            (context as AddNewLocationActivity).roomChosen.text = roomList[position].room
            context.roomLV.visibility = View.GONE
            //context.locationCreated.room = roomList[position]
            context.locationCreated = roomList[position]
            context.checkIfReady()
        }

        return convertView
    }

}
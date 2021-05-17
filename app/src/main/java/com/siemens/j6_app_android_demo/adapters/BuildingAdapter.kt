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

class BuildingAdapter(
    private val context: Context,
    private val buildingList: java.util.ArrayList<String>
) : BaseAdapter() {

    private lateinit var buildingName: TextView
    private lateinit var sepLine: View

    override fun getCount(): Int {
        return buildingList.size
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
        buildingName = convertView.findViewById(R.id.option_name)
        sepLine = convertView.findViewById(R.id.sep_line)
        buildingName.text = buildingList[position]
        if (position == buildingList.size-1)
            sepLine.visibility = View.GONE
        convertView.setOnClickListener {
            (context as AddNewLocationActivity).buildingChosen.text = buildingList[position]
            context.buildingLV.visibility = View.GONE
            //context.locationCreated.building = buildingList[position]
            context.checkIfReady()
            context.levelList.clear()
            context.levelList.addAll(ArrayList(context.locationMap[buildingList[position]]!!.keys))
            (context.levelLV.adapter as LevelAdapter).notifyDataSetChanged()
        }

        return convertView
    }

}
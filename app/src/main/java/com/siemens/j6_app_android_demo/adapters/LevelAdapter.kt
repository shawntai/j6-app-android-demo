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

class LevelAdapter(
    private val context: Context,
    private val levelList: java.util.ArrayList<String>
) : BaseAdapter() {

    private lateinit var levelName: TextView
    private lateinit var sepLine: View

    override fun getCount(): Int {
        return levelList.size
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
        levelName = convertView.findViewById(R.id.option_name)
        sepLine = convertView.findViewById(R.id.sep_line)
        levelName.text = levelList[position]
        if (position == levelList.size-1)
            sepLine.visibility = View.GONE
        convertView.setOnClickListener {
            (context as AddNewLocationActivity).levelChosen.text = levelList[position]
            context.levelLV.visibility = View.GONE
            //context.locationCreated.level = levelList[position]
            context.checkIfReady()
            context.roomList.clear()
            context.roomList.addAll(context.locationMap[context.buildingChosen.text]?.get(levelList[position])!!)
            (context.levelLV.adapter as LevelAdapter).notifyDataSetChanged()
        }

        return convertView
    }

}
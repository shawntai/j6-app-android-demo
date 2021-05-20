package com.siemens.j6_app_android_demo.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.models.Location
import java.util.*

class LocationRecyclerViewAdapter(var list: ArrayList<Location>) : RecyclerView.Adapter<LocationRecyclerViewAdapter.MyViewHolder>() {
    private var clickListener: ClickListener? = null

    // swipe
    private val viewBinderHelper: ViewBinderHelper? = ViewBinderHelper()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var building: TextView = view.findViewById(R.id.building)
        var level: TextView = view.findViewById(R.id.level)
        var room: TextView = view.findViewById(R.id.room)
        var bg: LinearLayout = view.findViewById(R.id.bg)
        var swipeRevealLayout: SwipeRevealLayout = view as SwipeRevealLayout//.findViewById(R.id.swipeRevealLayout)
        var deleteLoc: LinearLayout = view.findViewById(R.id.delete)
        init {
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            clickListener?.onItemClick(adapterPosition, v)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.loc_rv_item, parent, false)
        return MyViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //val date: Calendar = list[position]
        holder.building.text = list[position].building.name
        holder.level.text = list[position].level
        holder.room.text = list[position].room
        holder.bg.setBackgroundColor(Color.parseColor(if (list[position].isSelected) "#F36F32" else "#152341"))
        holder.bg.setOnClickListener {
            notifyDataSetChanged()
            if (!list[position].isSelected) {
                list[position].isSelected = true
                holder.bg.setBackgroundColor(Color.parseColor("#F36F32"))
            } else {
                list[position].isSelected = false
                holder.bg.setBackgroundColor(Color.parseColor("#152341"))
            }
        }
        holder.deleteLoc.setOnClickListener {
            list.removeAt(position)
            notifyDataSetChanged()
        }

        // swipe
        // You need to provide a String id which uniquely defines the data object.
        viewBinderHelper!!.bind(holder.swipeRevealLayout, list[position].getId())
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int, v: View?)
    }

}
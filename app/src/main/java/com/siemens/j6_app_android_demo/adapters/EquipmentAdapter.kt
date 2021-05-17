package com.siemens.j6_app_android_demo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.models.Equipment
import java.util.*

class EquipmentAdapter(var list: ArrayList<Equipment>) : RecyclerView.Adapter<EquipmentAdapter.MyViewHolder>() {
    private var clickListener: ClickListener? = null
    // swipe
    private val viewBinderHelper: ViewBinderHelper? = ViewBinderHelper()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var e_id: AutoCompleteTextView = view.findViewById(R.id.equip_id)
        var description: EditText  = view.findViewById(R.id.description)
        var downtime: EditText = view.findViewById(R.id.downtime)
        var condition: TextView = view.findViewById(R.id.condition)
        var swipeRevealLayout: SwipeRevealLayout = view as SwipeRevealLayout
        var delete: LinearLayout = view.findViewById(R.id.delete)
        init {
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            clickListener?.onItemClick(adapterPosition, v)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.equipment_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.e_id.setText(list[position].eid)
        holder.description.setText(list[position].description)
        holder.downtime.setText(list[position].downtime)
        holder.condition.text = list[position].condition
        holder.delete.setOnClickListener {
            list.removeAt(position)
            notifyDataSetChanged()
        }
        viewBinderHelper!!.bind(holder.swipeRevealLayout, list[position].id.toString())
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
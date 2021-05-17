package com.siemens.j6_app_android_demo.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.activities.home_page.AddNewWorkOrderActivity
import java.util.*

class CategorySelectionAdapter(var list: ArrayList<String>) : RecyclerView.Adapter<CategorySelectionAdapter.MyViewHolder>() {
    private var clickListener: ClickListener? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var optionName: TextView = view.findViewById(R.id.option_name)
        init {
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            clickListener?.onItemClick(adapterPosition, v)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.selection_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.optionName.text = list[position]
        holder.optionName.setOnClickListener {
            (holder.optionName.context as AddNewWorkOrderActivity).findViewById<TextView>(R.id.category).text = holder.optionName.text
            holder.optionName.setTextColor(Color.parseColor("#F36F32"))
        }
        /*holder.bg.setOnClickListener {
            if (!list[position].isSelected) {
                list[position].isSelected = true
                holder.bg.setBackgroundResource(R.drawable.shape_orange)
                holder.check.visibility = View.VISIBLE
            } else {
                list[position].isSelected = false
                holder.bg.setBackgroundResource(R.drawable.shape_gray_less_round)
                holder.check.visibility = View.INVISIBLE
            }
        }*/
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
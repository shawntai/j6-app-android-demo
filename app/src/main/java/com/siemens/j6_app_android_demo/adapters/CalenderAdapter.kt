package com.siemens.j6_app_android_demo.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.siemens.j6_app_android_demo.R
import java.util.*


class CalenderAdapter(var list: List<Calendar>) : RecyclerView.Adapter<CalenderAdapter.MyViewHolder>() {
    private val days = arrayOf("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    private var clickListener: ClickListener? = null
    private var indexClicked: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1  // today's date

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var date: TextView = view.findViewById(R.id.date)
        var day: TextView = view.findViewById(R.id.day)
        var bg: LinearLayout = view.findViewById(R.id.date_background)
        init {
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            clickListener?.onItemClick(adapterPosition, v)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.data_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val date: Calendar = list[position]
        holder.date.text = date.get(Calendar.DATE).toString()
        holder.day.text = days[date.get(Calendar.DAY_OF_WEEK)]
        holder.bg.setOnClickListener(View.OnClickListener {
            indexClicked = position
            notifyDataSetChanged()
        })
        Log.d("indexClicked", indexClicked.toString())
        Log.d("position", position.toString())
        if (indexClicked == position) {
            holder.bg.setBackgroundResource(R.drawable.shape_orange)
        } else {
            holder.bg.setBackgroundColor(Color.parseColor("#1a1a1a"))
        }
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
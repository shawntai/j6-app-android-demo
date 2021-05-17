package com.siemens.j6_app_android_demo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.models.Employee
import java.util.*

class EmployeeAdapter(var list: ArrayList<Employee>) : RecyclerView.Adapter<EmployeeAdapter.MyViewHolder>() {
    private var clickListener: ClickListener? = null
    private var indexClicked: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1  // today's date

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var name: TextView = view.findViewById(R.id.name)
        var title: TextView  = view.findViewById(R.id.title)
        var department: TextView = view.findViewById(R.id.department)
        var email: TextView = view.findViewById(R.id.email)
        var check: ImageView = view.findViewById(R.id.check)
        var image: ImageView = view.findViewById(R.id.image)
        var bg: LinearLayout = view.findViewById(R.id.bg)
        init {
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            clickListener?.onItemClick(adapterPosition, v)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.employee_lv_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.name.text = list[position].name
        holder.title.text = list[position].position
        holder.department.text = list[position].department
        holder.email.text = list[position].email
        Glide.with(holder.image.context).load(list[position].imageUrl).fitCenter().override(SIZE_ORIGINAL, SIZE_ORIGINAL).into(holder.image)
        if (list[position].isSelected) {
            holder.bg.setBackgroundResource(R.drawable.shape_orange)
            holder.check.visibility = View.VISIBLE
        } else {
            holder.bg.setBackgroundResource(R.drawable.shape_gray_less_round)
            holder.check.visibility = View.INVISIBLE
        }
        holder.bg.setOnClickListener {
            if (!list[position].isSelected) {
                list[position].isSelected = true
                holder.bg.setBackgroundResource(R.drawable.shape_orange)
                holder.check.visibility = View.VISIBLE
            } else {
                list[position].isSelected = false
                holder.bg.setBackgroundResource(R.drawable.shape_gray_less_round)
                holder.check.visibility = View.INVISIBLE
            }
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
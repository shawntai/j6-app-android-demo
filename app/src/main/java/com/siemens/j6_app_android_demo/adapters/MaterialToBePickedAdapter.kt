package com.siemens.j6_app_android_demo.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.activities.home_page.SelectMaterialActivity
import com.siemens.j6_app_android_demo.models.Material
import java.util.*

class MaterialToBePickedAdapter(var list: ArrayList<Material>) : RecyclerView.Adapter<MaterialToBePickedAdapter.MyViewHolder>() {
    private var clickListener: ClickListener? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var name: TextView = view.findViewById(R.id.name)
        var code: TextView  = view.findViewById(R.id.code)
        var unitPrice: TextView = view.findViewById(R.id.unitRate)
        var location: TextView = view.findViewById(R.id.location)
        var qty: TextView = view.findViewById(R.id.qty)
        var image: ImageView = view.findViewById(R.id.mat_image)
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
            .inflate(R.layout.material_item_to_be_picked, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = list[position].name
        holder.code.text = list[position].code
        holder.unitPrice.text = list[position].unitRate.toString()
        holder.qty.text = list[position].qty.toString()
        Glide.with(holder.image.context).load(list[position].imageUrl).fitCenter().override(
            Target.SIZE_ORIGINAL,
            Target.SIZE_ORIGINAL
        ).into(holder.image)
        holder.bg.setBackgroundColor(Color.parseColor(if (list[position].isSelected) "#f36f32" else "#454545"))
        holder.bg.setOnClickListener {
            if (!list[position].isSelected) {
                list[position].isSelected = true
                holder.bg.setBackgroundColor(Color.parseColor("#f36f32"))
            } else {
                list[position].isSelected = false
                holder.bg.setBackgroundColor(Color.parseColor("#454545"))
            }
            (holder.bg.context as SelectMaterialActivity).updateButtonColor()
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
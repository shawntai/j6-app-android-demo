package com.siemens.j6_app_android_demo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.models.Material
import java.util.*

class AddMaterialAdapter(var list: ArrayList<Material>) : RecyclerView.Adapter<AddMaterialAdapter.MyViewHolder>() {
    private var clickListener: ClickListener? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var name: TextView = view.findViewById(R.id.name)
        var code: TextView  = view.findViewById(R.id.code)
        var unitRate: TextView = view.findViewById(R.id.unitRate)
        var qty: TextView = view.findViewById(R.id.qty)
        var plus: ImageView = view.findViewById(R.id.plus)
        var minus: ImageView = view.findViewById(R.id.minus)
        var image: ImageView = view.findViewById(R.id.mat_image)
        init {
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            clickListener?.onItemClick(adapterPosition, v)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.add_material_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //val date: Calendar = list[position]
        holder.name.text = list[position].name
        holder.code.text = list[position].code
        holder.unitRate.text = list[position].unitRate.toString()
        holder.qty.text = list[position].qty.toString()
        holder.plus.setOnClickListener {
            list[position].qty++
            holder.qty.text = list[position].qty.toString()
        }
        holder.minus.setOnClickListener {
            if(list[position].qty > 0) {
                list[position].qty--
                holder.qty.text = list[position].qty.toString()
            }
        }
        Glide.with(holder.image.context).load(list[position].imageUrl).fitCenter().override(
            Target.SIZE_ORIGINAL,
            Target.SIZE_ORIGINAL
        ).into(holder.image)
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
package com.siemens.j6_app_android_demo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.models.Material
import java.util.*

class MaterialToBePickedAdapter(var list: ArrayList<Material>) : RecyclerView.Adapter<MaterialToBePickedAdapter.MyViewHolder>() {
    private var clickListener: ClickListener? = null

    private val viewBinderHelper: ViewBinderHelper? = ViewBinderHelper()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var name: TextView = view.findViewById(R.id.name)
        var code: TextView  = view.findViewById(R.id.code)
        var unitPrice: TextView = view.findViewById(R.id.unitRate)
        var qty: TextView = view.findViewById(R.id.qty)
        var image: ImageView = view.findViewById(R.id.mat_image)
        var bg: LinearLayout = view.findViewById(R.id.bg)
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
            .inflate(R.layout.material_item_to_be_picked, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = list[position].name
        holder.code.text = list[position].code
        holder.unitPrice.text = list[position].unitRate.toString()
        holder.qty.text = list[position].qty.toString()
        holder.bg.setOnClickListener {  }
        holder.delete.setOnClickListener {
            list.removeAt(position)
            notifyDataSetChanged()
        }
        list[position].isSelected = true
        Glide.with(holder.image.context).load(list[position].imageUrl).fitCenter().override(
            Target.SIZE_ORIGINAL,
            Target.SIZE_ORIGINAL
        ).into(holder.image)
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
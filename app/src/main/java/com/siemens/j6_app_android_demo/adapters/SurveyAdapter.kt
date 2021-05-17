package com.siemens.j6_app_android_demo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.models.Survey
import java.util.*

class SurveyAdapter(var list: ArrayList<Survey>) : RecyclerView.Adapter<SurveyAdapter.MyViewHolder>() {
    private var clickListener: ClickListener? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var name: TextView = view.findViewById(R.id.feedback_name)
        var stars: LinearLayout = view.findViewById(R.id.stars)
        init {
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            clickListener?.onItemClick(adapterPosition, v)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.feedback_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = list[position].item
        // initialize stars
        for (i in 0 until holder.stars.size) {
            val starToSet = holder.stars[i] as ImageView
            if (i<list[position].score) {
                starToSet.setImageResource(R.drawable.lit_star)
            } else {
                starToSet.setImageResource(R.drawable.dark_star)
            }
        }
        // set onStarsClick
        for (i in 0 until holder.stars.size) {
            val star = holder.stars[i] as ImageView
            star.setOnClickListener {
                list[position].score = i+1
                for (j in 0 until holder.stars.size) {
                    val starToSet = holder.stars[j] as ImageView
                    if (j<=i) {
                        starToSet.setImageResource(R.drawable.lit_star)
                    } else {
                        starToSet.setImageResource(R.drawable.dark_star)
                    }
                }
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
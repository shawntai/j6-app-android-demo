package com.siemens.j6_app_android_demo.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.models.Attachment
import java.util.*

class AttachmentAdapter(var list: ArrayList<Attachment>) : RecyclerView.Adapter<AttachmentAdapter.MyViewHolder>() {
    private var clickListener: ClickListener? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
        var extension: TextView = view.findViewById(R.id.extension)
        var fileName: TextView = view.findViewById(R.id.file_name)
        init {
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            clickListener?.onItemClick(adapterPosition, v)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.attachment_item, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (list[position].type == Attachment.IMAGE && list[position].attachment is Uri) {
            holder.thumbnail.setImageURI(list[position].attachment as Uri)
        } else if (list[position].type == Attachment.CAMERA) {
            holder.thumbnail.setImageBitmap(list[position].attachment as Bitmap)
        }
        holder.fileName.text = list[position].fileName
        holder.extension.text = "Extension: " + list[position].extension
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
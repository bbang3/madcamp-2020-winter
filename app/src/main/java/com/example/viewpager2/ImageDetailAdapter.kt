package com.example.viewpager2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ImageDetailAdapter(val imageList: MutableList<Image>) : RecyclerView.Adapter<ImageDetailAdapter.ImageDetailViewHolder>() {
    class ImageDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById<ImageView>(R.id.gallery_detail_image)
        val name: TextView = itemView.findViewById<TextView>(R.id.gallery_detail_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_detail_item, parent, false)
        return ImageDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageDetailViewHolder, position: Int) {
        val currentImage = imageList[position]
        if(currentImage.image != null)
            holder.image.setImageResource(currentImage.image)
        else holder.image.setImageResource(R.drawable.not_found_image)

        holder.name.text = currentImage.name
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}
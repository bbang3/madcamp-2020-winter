package com.example.viewpager2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(val imageList: ArrayList<Image>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageAdapter.ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ImageAdapter.ImageViewHolder, position: Int) {
        imageList[position].image?.let { holder.image.setImageResource(it) }
        holder.image.setOnClickListener {
            val intent = Intent(holder.image.context, ImageDetailActivity::class.java)
            intent.putExtra("position", position)
            intent.putParcelableArrayListExtra("image_list", imageList)

            holder.image.context.startActivity(intent)
        }
    }

    class ImageViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imageView)
    }
}

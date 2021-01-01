package com.example.viewpager2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
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
        holder.image.setImageResource(imageList[position].imageID)
        holder.image.setOnClickListener {
            // TODO: zoom in selected image
            val intent = Intent(holder.image.context, ImageDetailActivity::class.java)
            intent.putExtra("name", imageList[position].name)
            intent.putExtra("imageID", imageList[position].imageID)
            ContextCompat.startActivity(holder.image.context, intent, null)
//            Toast.makeText(holder.image.context, "${imageList[position].name}", Toast.LENGTH_SHORT).show()
        }
    }

    class ImageViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imageView)
    }
}

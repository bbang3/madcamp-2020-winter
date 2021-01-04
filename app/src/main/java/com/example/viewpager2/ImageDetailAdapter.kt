package com.example.viewpager2


import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.theartofdev.edmodo.cropper.CropImage

class ImageDetailAdapter(val context: Context, val imageList: MutableList<Image>) : RecyclerView.Adapter<ImageDetailAdapter.ImageDetailViewHolder>() {

    class ImageDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById<ImageView>(R.id.gallery_detail_image)
        val name: TextView = itemView.findViewById<TextView>(R.id.gallery_detail_name)
        val cropbtn : ImageButton = itemView.findViewById<ImageButton>(R.id.cropbtn)
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

        holder.cropbtn.setOnClickListener {
            val imageId: Int? = currentImage.image
            if(imageId != null){
                var uri: Uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.resources.getResourcePackageName(imageId) + '/' + context.resources.getResourceTypeName(imageId) + '/' + context.resources.getResourceEntryName(imageId) )
                CropImage.activity(uri).start(context as Activity)
            }
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }


    companion object {
        const val crop = "com.example.viewpager2.crop"
    }

}
package com.example.viewpager2

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.app.Activity
import android.net.Uri
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.recyclerview.widget.RecyclerView
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File
import java.security.AccessController.getContext

class ImageDetailAdapter(val context: Context, val imageList: MutableList<Image>) : RecyclerView.Adapter<ImageDetailAdapter.ImageDetailViewHolder>() {

    class ImageDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById<ImageView>(R.id.gallery_detail_image)
        val name: TextView = itemView.findViewById<TextView>(R.id.gallery_detail_name)
        val cropbtn : ImageButton = itemView.findViewById<ImageButton>(R.id.cropbtn)
        val sharebtn : ImageButton = itemView.findViewById<ImageButton>(R.id.sharebtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageDetailViewHolder {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
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

        holder.sharebtn.setOnClickListener {
            val intent = Intent()
            val image = currentImage.image
            val resources = context.resources
            val uri: Uri = Uri.parse(
                "content://"+resources.getResourcePackageName(image!!) + '/'+resources.getResourceTypeName(image)+'/'
                    +resources.getResourceEntryName(image))
            Log.i("aaaaUri",uri.toString())
            val finalUri  = FileProvider.getUriForFile(context, "com.bignerdranch.android.test.fileprovider", uri.toFile())
            Log.i("aaaafinalUri",finalUri.toString())
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_STREAM,finalUri)
            intent.type = "image/*"
            context.startActivity(Intent.createChooser(intent, "Please select app "))
        }

    }

    override fun getItemCount(): Int {
        return imageList.size
    }


    companion object {
        const val crop = "com.example.viewpager2.crop"
    }

}
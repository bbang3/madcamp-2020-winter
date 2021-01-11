package com.example.madstagrarn.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madstagrarn.Post
import com.example.madstagrarn.R
import com.example.madstagrarn.network.DataService

class PostAdapter (private val postList: ArrayList<Post>, private val dataService: DataService) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.post_image)
        var description: TextView = itemView.findViewById(R.id.post_description)

        fun bind(post : Post, dataService: DataService) {
            description.text = post.description

            Glide.with(itemView)
                .load("${dataService.BASE_URL}image/${post.images[0]}")
                .thumbnail(0.1f)
                .into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mypost_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = postList[position]
        holder.bind(currentItem, dataService)
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}
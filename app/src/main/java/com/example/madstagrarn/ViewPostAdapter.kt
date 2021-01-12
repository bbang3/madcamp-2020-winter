package com.example.madstagrarn


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madstagrarn.dataclass.Post
import com.example.madstagrarn.R
import com.example.madstagrarn.dataclass.User
import com.example.madstagrarn.network.DataService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.coroutineContext

class ViewPostAdapter(private val postList: ArrayList<Post>, var currentUser: User, private val dataService: DataService) :
    RecyclerView.Adapter<ViewPostAdapter.PostViewHolder>() {
    private lateinit var view: View

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.view_post_image)
        var description: TextView = itemView.findViewById(R.id.view_post_description)
        var postDate: TextView = itemView.findViewById(R.id.view_post_date)
        val author: TextView = itemView.findViewById(R.id.view_post_author)
        val profileImageView: ImageView = itemView.findViewById(R.id.view_post_profile)

        fun bind(post: Post, currentUser: User, dataService: DataService) {
            description.text = post.description
            postDate.text = post.date
            author.text = post.author

            if(currentUser.profileImage.isNullOrEmpty() || currentUser.profileImage == "default_user_profile.png") {
                Glide.with(itemView)
                    .load(R.drawable.profile)
                    .circleCrop()
                    .placeholder(R.drawable.loading_spinner)
                    .into(profileImageView)
            } else {
                Glide.with(itemView)
                    .load(dataService.BASE_URL + "image/${currentUser.profileImage}")
                    .circleCrop()
                    .placeholder(R.drawable.loading_spinner)
                    .into(profileImageView)
            }

            Glide.with(itemView)
                .load("${dataService.BASE_URL}image/${post.images[0]}")
                .placeholder(R.drawable.loading_spinner)
                .into(image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.view_post_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = postList[position]
        holder.bind(currentItem, currentUser, dataService)

    }

    override fun getItemCount(): Int {
        return postList.size
    }

}
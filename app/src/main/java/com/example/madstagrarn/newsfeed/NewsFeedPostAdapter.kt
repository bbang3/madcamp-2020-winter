package com.example.madstagrarn.newsfeed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madstagrarn.dataclass.Post
import com.example.madstagrarn.R
import com.example.madstagrarn.network.DataService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFeedPostAdapter(private val postList: ArrayList<Post>, private val dataService: DataService) :
    RecyclerView.Adapter<NewsFeedPostAdapter.NewsFeedPostViewHolder>() {
    class NewsFeedPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.newsfeed_post_image)
        var description: TextView = itemView.findViewById(R.id.newsfeed_post_description)
        var postDate: TextView = itemView.findViewById(R.id.newsfeed_post_date)
        val author: TextView = itemView.findViewById(R.id.newsfeed_post_author)
        val profileImageView: ImageView = itemView.findViewById(R.id.newsfeed_post_profile)

        fun bind(post: Post, dataService: DataService) {
            description.text = post.description
            postDate.text = post.date
            author.text = post.author

            Glide.with(itemView)
                .load(dataService.BASE_URL + "api/user/${post.authorId}/profile")
                .thumbnail()
                .circleCrop()
                .into(profileImageView)

            Glide.with(itemView)
                .load("${dataService.BASE_URL}image/${post.images[0]}")
                .thumbnail()
                .into(image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsFeedPostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.newsfeed_post_item, parent, false)
        return NewsFeedPostViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsFeedPostViewHolder, position: Int) {
        val currentItem = postList[position]
        holder.bind(currentItem, dataService)

    }

    override fun getItemCount(): Int {
        return postList.size
    }
}
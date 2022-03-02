package com.example.madstagrarn.newsfeed

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madstagrarn.ParseDate
import com.example.madstagrarn.dataclass.Post
import com.example.madstagrarn.R
import com.example.madstagrarn.UserViewActivity
import com.example.madstagrarn.dataclass.User
import com.example.madstagrarn.network.DataService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewsFeedPostAdapter(private val postList: ArrayList<Post>, private val dataService: DataService) :
    RecyclerView.Adapter<NewsFeedPostAdapter.NewsFeedPostViewHolder>() {
    class NewsFeedPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.newsfeed_post_image)
        var description: TextView = itemView.findViewById(R.id.newsfeed_post_description)
        var postDate: TextView = itemView.findViewById(R.id.newsfeed_post_date)
        val author: TextView = itemView.findViewById(R.id.newsfeed_post_author)
        val profileImageView: ImageView = itemView.findViewById(R.id.newsfeed_post_profile)
        var container: LinearLayout = itemView.findViewById(R.id.newsfeed_profile_container)

        fun bind(post: Post, dataService: DataService) {
            description.text = post.description
            postDate.text = ParseDate.dateConvert(post.date)
            author.text = post.author
            container.setOnClickListener { view ->
                dataService.service.getUser(post.authorId).enqueue(object: Callback<User>{
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        val user: User = response.body()!!
                        val intent = Intent(view.context, UserViewActivity::class.java)
                        intent.putExtra("User", user)
                        view.context.startActivity(intent)
                    }
                    override fun onFailure(call: Call<User>, t: Throwable) {
                        t.printStackTrace()
                        Toast.makeText(view.context, "Failed to load user information", Toast.LENGTH_SHORT).show()
                    }
                })
            }

            Glide.with(itemView)
                .load(dataService.BASE_URL + "api/user/${post.authorId}/profile")
                .circleCrop()
                .placeholder(R.drawable.loading_spinner)
                .into(profileImageView)

            Glide.with(itemView)
                .load("${dataService.BASE_URL}image/${post.images[0]}")
                .placeholder(R.drawable.loading_spinner)
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
package com.example.madstagrarn.mypage

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
import com.example.madstagrarn.dataclass.User
import com.example.madstagrarn.network.DataService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostAdapter(private val postList: ArrayList<Post>, private val currentUser: User, private val dataService: DataService) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.post_image)
        var description: TextView = itemView.findViewById(R.id.post_description)
        var postDate: TextView = itemView.findViewById(R.id.post_date)
        val author: TextView = itemView.findViewById(R.id.post_author)
        val profileImageView: ImageView = itemView.findViewById(R.id.post_profile)
        var delButton: Button = itemView.findViewById(R.id.post_delete_button)

        fun bind(post: Post, currentUser: User, dataService: DataService) {
            description.text = post.description
            postDate.text = post.date
            author.text = post.author

            if(currentUser.profileImage.isNullOrEmpty() || currentUser.profileImage == "default_user_profile.png") {
                Glide.with(itemView)
                    .load(R.drawable.person_profile)
                    .circleCrop()
                    .into(profileImageView)
            } else {
                Glide.with(itemView)
                    .load(dataService.BASE_URL + "image/${currentUser.profileImage}")
                    .thumbnail()
                    .circleCrop()
                    .into(profileImageView)
            }

            Glide.with(itemView)
                .load("${dataService.BASE_URL}image/${post.images[0]}")
                .thumbnail()
                .into(image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mypost_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = postList[position]
        holder.bind(currentItem, currentUser, dataService)

        holder.delButton.setOnClickListener { deletePost(currentItem._id, position) }
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    private fun deletePost(postId: String, position: Int) {
        dataService.service.deletePost(postId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    postList.removeAt(position)
                    notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
}
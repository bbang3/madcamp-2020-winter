package com.example.madstagrarn.mypage

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

class PostAdapter(private val postList: ArrayList<Post>, var currentUser: User, private val dataService: DataService) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private lateinit var view: View

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.post_image)
        var description: TextView = itemView.findViewById(R.id.post_description)
        var postDate: TextView = itemView.findViewById(R.id.post_date)
        val author: TextView = itemView.findViewById(R.id.post_author)
        val profileImageView: ImageView = itemView.findViewById(R.id.post_profile)
        var delButton: ImageButton = itemView.findViewById(R.id.post_delete_button)

        fun bind(post: Post, currentUser: User, dataService: DataService) {
            description.text = post.description
            postDate.text = post.date
            author.text = post.author

            if(currentUser.profileImage.isNullOrEmpty() || currentUser.profileImage == "default_user_profile.png") {
                Glide.with(itemView)
                    .load(R.drawable.profile)
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
        view = LayoutInflater.from(parent.context).inflate(R.layout.mypost_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = postList[position]
        holder.bind(currentItem, currentUser, dataService)

        holder.delButton.setOnClickListener {
            val inflater = holder.itemView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.mypage_post_delete_popup, null)
            val deleteButton = view.findViewById<Button>(R.id.delete_button)
            val cancelButton = view.findViewById<Button>(R.id.cancel_button)

            val deletePopup = AlertDialog.Builder(holder.itemView.context)
                .create()

            deletePopup?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            deleteButton.setOnClickListener {
                deletePost(currentItem._id, position)
                deletePopup.dismiss()
            }

            cancelButton.setOnClickListener {
                deletePopup.dismiss()
            }


            deletePopup.setView(view)
            deletePopup.show()
        }
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
package com.example.madstagrarn.following

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madstagrarn.R
import com.example.madstagrarn.UserViewActivity
import com.example.madstagrarn.dataclass.User
import com.example.madstagrarn.network.DataService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignedUserAdapter(
    private val userList: ArrayList<User>,
    private val followingIds: ArrayList<String>,
    private var currentUser: User
) : RecyclerView.Adapter<SignedUserAdapter.SignedUserViewHolder>() {
    private val dataService: DataService = DataService()

    class SignedUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name_text)
        var phoneNumber: TextView = itemView.findViewById(R.id.phonenumber_text)
        var profileImage: ImageView = itemView.findViewById(R.id.following_profile_image)
        var followButton: Button = itemView.findViewById(R.id.follow_button)
        var container: LinearLayout = itemView.findViewById(R.id.signed_profile_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignedUserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.signed_user_item, parent, false)
        return SignedUserViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: SignedUserViewHolder, position: Int) {
        val currentItem = userList[position]

        holder.name.text = currentItem.name
        holder.phoneNumber.text = currentItem.phoneNumber
        holder.container.setOnClickListener { view ->
            val intent = Intent(view.context, UserViewActivity::class.java)
            intent.putExtra("User", currentItem)
            view.context.startActivity(intent)
        }


        if(currentItem.profileImage.isNullOrEmpty() || currentItem.profileImage == "default_user_profile.png") {
            Glide.with(holder.itemView)
                .load(R.drawable.person)
                .circleCrop()
                .placeholder(R.drawable.loading_spinner)
                .into(holder.profileImage)
        } else {
            Glide.with(holder.itemView)
                .load(dataService.BASE_URL + "image/${currentItem.profileImage}")
                .circleCrop()
                .placeholder(R.drawable.loading_spinner)
                .into(holder.profileImage)
        }

        if (followingIds.contains(currentItem._id)) {
            holder.followButton.setBackgroundColor(Color.parseColor("#FFE4E6EB"))
            holder.followButton.setTextColor(Color.BLACK)
            holder.followButton.text = "Unfollow"
        } else {
            holder.followButton.setBackgroundColor(Color.parseColor("#FF1877F2"))
            holder.followButton.setTextColor(Color.WHITE)
            holder.followButton.text = "Follow"
        }
        holder.followButton.setOnClickListener {
            if (holder.followButton.text == "Follow") {
                holder.followButton.setBackgroundColor(Color.parseColor("#FFE4E6EB"))
                holder.followButton.setTextColor(Color.BLACK)
                holder.followButton.text = "Unfollow"
                dataService.service.followRequest(currentUser._id, currentItem._id)
                    .enqueue(object : Callback<User> {
                        override fun onFailure(call: Call<User>, t: Throwable) {
                            t.printStackTrace()
                        }

                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            if (response.isSuccessful) {
                            }
                        }

                    })
            } else if(holder.followButton.text == "Unfollow") {
                holder.followButton.setBackgroundColor(Color.parseColor("#FF1877F2"))
                holder.followButton.setTextColor(Color.WHITE)
                holder.followButton.text = "Follow"
                dataService.service.unfollowRequest(currentUser._id, currentItem._id)
                    .enqueue(object : Callback<User> {
                        override fun onFailure(call: Call<User>, t: Throwable) {
                            t.printStackTrace()
                        }

                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            if (response.isSuccessful) {
                            }
                        }

                    })
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
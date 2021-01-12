package com.example.madstagrarn.following

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madstagrarn.R
import com.example.madstagrarn.dataclass.User
import com.example.madstagrarn.network.DataService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingAdapter(
    private val followingUserList: ArrayList<User>,
    private val currentUser: User
) : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {
    private val dataService: DataService = DataService()

    class FollowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name_text)
        var phoneNumber: TextView = itemView.findViewById(R.id.phonenumber_text)
        var profileImage: ImageView = itemView.findViewById(R.id.following_profile_image)
        var followButton: Button = itemView.findViewById(R.id.follow_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.following_item, parent, false)
        return FollowingViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        val currentItem = followingUserList[position]

        holder.name.text = currentItem.name
        holder.phoneNumber.text = currentItem.phoneNumber

        if(currentItem.profileImage.isNullOrEmpty() || currentItem.profileImage == "default_user_profile.png") {
            Glide.with(holder.itemView)
                .load(R.drawable.person)
                .circleCrop()
                .into(holder.profileImage)
        } else {
            Glide.with(holder.itemView)
                .load(dataService.BASE_URL + "image/${currentItem.profileImage}")
                .thumbnail()
                .circleCrop()
                .into(holder.profileImage)
        }

        if (true) {
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
            } else if (holder.followButton.text == "Unfollow") {
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
        return followingUserList.size
    }
}
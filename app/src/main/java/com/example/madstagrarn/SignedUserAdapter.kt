package com.example.madstagrarn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FollowingAddAdapter (private val phoneList: ArrayList<Phone>, private val userList: ArrayList<User>)
    : RecyclerView.Adapter<FollowingAddAdapter.FollowingAddViewHolder>() {
    class FollowingAddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name_text)
        var phoneNumber: TextView = itemView.findViewById(R.id.phonenumber_text)
        var profileImage: ImageView = itemView.findViewById(R.id.profile_image)
        var addButton: ImageButton = itemView.findViewById(R.id.add_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingAddViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.following_item, parent, false)
        return FollowingAddViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowingAddViewHolder, position: Int) {
        val currentItem = phoneList[position]

        holder.name.text = currentItem.name
        holder.phoneNumber.text = currentItem.phoneNumber

        holder.profileImage.setImageResource(R.drawable.madstagrarn_logo)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
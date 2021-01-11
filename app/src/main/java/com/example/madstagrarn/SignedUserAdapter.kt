package com.example.madstagrarn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SignedUserAdapter (private val userList: ArrayList<User>)
    : RecyclerView.Adapter<SignedUserAdapter.SignedUserViewHolder>() {
    class SignedUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name_text)
        var phoneNumber: TextView = itemView.findViewById(R.id.phonenumber_text)
        var profileImage: ImageView = itemView.findViewById(R.id.profile_image)
        var followButton: ImageButton = itemView.findViewById(R.id.follow_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignedUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.signed_user_item, parent, false)
        return SignedUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: SignedUserViewHolder, position: Int) {
        val currentItem = userList[position]

        holder.name.text = currentItem.name
        holder.phoneNumber.text = currentItem.phoneNumber
        holder.profileImage.setImageResource(R.drawable.madstagrarn_logo)

        holder.followButton.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
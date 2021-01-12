package com.example.madstagrarn.following

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.recyclerview.widget.RecyclerView
import com.example.madstagrarn.dataclass.Phone
import com.example.madstagrarn.R
import java.lang.Exception


class UnsignedUserAdapter (private val phoneList: ArrayList<Phone>)
    : RecyclerView.Adapter<UnsignedUserAdapter.UnsignedUserViewHolder>() {
    private var smsManager: SmsManager = SmsManager.getDefault()

    class UnsignedUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name_text)
        var phoneNumber: TextView = itemView.findViewById(R.id.phonenumber_text)
        var profileImage: ImageView = itemView.findViewById(R.id.following_profile_image)
        var inviteButton: Button = itemView.findViewById(R.id.invite_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnsignedUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.unsigned_user_item, parent, false)
        return UnsignedUserViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: UnsignedUserViewHolder, position: Int) {
        val currentItem = phoneList[position]

        holder.name.text = currentItem.name
        holder.phoneNumber.text = currentItem.phoneNumber
        holder.profileImage.setImageResource(R.drawable.person)
        holder.inviteButton.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(holder.itemView.context.checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                    try{
                        smsManager.sendTextMessage(currentItem.phoneNumber, null, "Hi, " + currentItem.name + ". Take a look at this awesome application Healthtagram!" + "\nhttps://bit.ly/3bwGebV", null, null)
                        Toast.makeText(holder.itemView.context, "Message is sent", Toast.LENGTH_LONG).show()
                    } catch (e: Exception){
                        Toast.makeText(holder.itemView.context, "Failed to send message", Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    requestPermissions(holder.itemView.context as Activity, arrayOf(Manifest.permission.SEND_SMS), 1)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return phoneList.size
    }
}
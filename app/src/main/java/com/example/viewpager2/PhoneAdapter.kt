package com.example.viewpager2

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.phone_item.view.*

class PhoneAdapter(val list: List<Phone>) : RecyclerView.Adapter<PhoneAdapter.Holder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    @SuppressLint("MissingPermission")
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mPhone: Phone? = null

        init {
            itemView.btnPhone.setOnClickListener {
                mPhone?.phone.let { phoneNumber ->
                    val uri = Uri.parse("tel:${phoneNumber.toString()}")
                    val intent = Intent(Intent.ACTION_CALL, uri)
                    itemView.context.startActivity(intent)
                }
            }
        }

        fun setPhone(phone: Phone) {
            this.mPhone = phone
            itemView.profile_image.setImageResource(R.drawable.ic_outline_account_circle_24)
            itemView.textName.text = phone.name
            itemView.textPhone.text = phone.phone
            itemView.btnPhone.setImageResource(R.drawable.ic_outline_call_24)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.phone_item, parent, false)
        return Holder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentItem = list[position]

        holder.setPhone(currentItem)
        holder.itemView.setOnClickListener { v ->
            itemClick?.onClick(v, position)
        }
    }


}

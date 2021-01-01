package com.example.viewpager2

import androidx.lifecycle.ViewModel

class PhoneViewModel : ViewModel() {
    private var phoneList: ArrayList<Phone> = arrayListOf(Phone("1", "1", "1"))

    fun getImageForId(id: String): Phone? {
        return phoneList.firstOrNull { it.id == id }
    }

    fun getPhoneList(): ArrayList<Phone> {
        return phoneList
    }
}
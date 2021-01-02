package com.example.viewpager2

import androidx.lifecycle.ViewModel

class PhoneViewModel : ViewModel() {
    private var phoneList: MutableList<Phone> = ArrayList()

    fun getImageForId(id: String): Phone? {
        return phoneList.firstOrNull { it.id == id }
    }

    fun getPhoneList(): MutableList<Phone> {
        return phoneList
    }
}
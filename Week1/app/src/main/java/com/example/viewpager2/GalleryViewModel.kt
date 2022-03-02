package com.example.viewpager2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel() {
    private val initialImageList: ArrayList<Image> = initialImageList()
    private var imageList: MutableList<Image> = initialImageList

    private fun initialImageList(): ArrayList<Image> {
        return arrayListOf(
            Image(id = 1, name = "img1", image = R.drawable.img1),
            Image(id = 2, name = "img2", image = R.drawable.img2),
            Image(id = 3, name = "img3", image = R.drawable.img3),
            Image(id = 4, name = "img4", image = R.drawable.img4),
            Image(id = 5, name = "img5", image = R.drawable.img5),
            Image(id = 6, name = "img6", image = R.drawable.img6),
            Image(id = 7, name = "img7", image = R.drawable.img7),
            Image(id = 8, name = "img8", image = R.drawable.img8),
            Image(id = 8, name = "img9", image = R.drawable.img9),
        )
    }

    fun getImageForId(id: Long): Image? {
        return imageList.firstOrNull { it.id == id }
    }

    fun getImageList(): MutableList<Image> {
        return imageList
    }
}
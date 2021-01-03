package com.example.viewpager2

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

class ImageDetailActivity : AppCompatActivity() {
    private var imageList: ArrayList<Image> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)
        supportActionBar?.hide()

        val bundle = intent.extras
        imageList.addAll(bundle?.getParcelableArrayList<Image>("image_list")!!)

        var rvGalleryDetail = findViewById<RecyclerView>(R.id.gallery_detail)
        rvGalleryDetail.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvGalleryDetail) // scroll image one by one

        rvGalleryDetail.setHasFixedSize(true)
        rvGalleryDetail.adapter = ImageDetailAdapter(this, imageList)

        val position = bundle?.getInt("position")
        position?.let { rvGalleryDetail.scrollToPosition(it) }


    }
}
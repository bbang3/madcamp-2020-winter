package com.example.viewpager2

<<<<<<< Updated upstream
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class ImageDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        val name = findViewById<TextView>(R.id.gallery_detail_name)
        val image = findViewById<ImageView>(R.id.gallery_detail_image)
=======
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
    private val galleryViewModel: GalleryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)
        supportActionBar?.hide()

        var rvGalleryDetail = findViewById<RecyclerView>(R.id.gallery_detail)
        rvGalleryDetail.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvGalleryDetail) // scroll image one by one

        rvGalleryDetail.setHasFixedSize(true)
        rvGalleryDetail.adapter = ImageDetailAdapter(this, galleryViewModel.getImageList())
>>>>>>> Stashed changes

        val bundle = intent.extras
        val currentImageName: String? = bundle?.getString("name")
        val currentImageID: Int? = bundle?.getInt("imageID")

        name.text = currentImageName
        if(currentImageID != null)
            image.setImageResource(currentImageID)
    }
}
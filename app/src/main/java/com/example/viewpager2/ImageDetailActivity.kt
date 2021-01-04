package com.example.viewpager2

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.theartofdev.edmodo.cropper.CropImage

class ImageDetailActivity : AppCompatActivity() {
    private var imageList: ArrayList<Image> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)
        supportActionBar?.hide()

        val bundle = intent.extras
        imageList.addAll(bundle?.getParcelableArrayList<Image>("image_list")!!)

        var rvGalleryDetail = findViewById<RecyclerView>(R.id.gallery_detail)
        rvGalleryDetail.setHasFixedSize(true)
        rvGalleryDetail.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvGalleryDetail) // scroll image one by one

        val adapter = ImageDetailAdapter(this, imageList)
        rvGalleryDetail.adapter = adapter


        val position = bundle?.getInt("position")
        position?.let { rvGalleryDetail.scrollToPosition(it) }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val croppedImageUri = result.uri
                val intent:Intent = Intent(this, CroppedImageActivity::class.java)
                intent.putExtra("uri", croppedImageUri)
                startActivity(intent)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                error.printStackTrace()
            }
        }
    }
}
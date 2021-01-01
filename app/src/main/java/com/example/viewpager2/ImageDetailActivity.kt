package com.example.viewpager2

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

        val bundle = intent.extras
        val currentImageName: String? = bundle?.getString("name")
        val currentImageID: Int? = bundle?.getInt("imageID")

        name.text = currentImageName
        if(currentImageID != null)
            image.setImageResource(currentImageID)
    }
}
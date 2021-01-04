package com.example.viewpager2

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CroppedImageActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_cropped)
        supportActionBar?.hide()

        var bundle: Bundle? = intent.getExtras()
        val croppedImageUri: Uri = bundle?.get("uri") as Uri

        findViewById<ImageView>(R.id.cropped_image).setImageURI(croppedImageUri)
    }
}


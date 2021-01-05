package com.example.viewpager2

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import kotlinx.android.synthetic.main.activity_image_cropped.*
import java.security.AccessController.getContext

class CroppedImageActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_cropped)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        supportActionBar?.hide()
        
        var bundle: Bundle? = intent.getExtras()
        val croppedImageUri: Uri = bundle?.get("uri") as Uri
        val finalUri  = FileProvider.getUriForFile(this, "com.bignerdranch.android.test.fileprovider", croppedImageUri.toFile())
        findViewById<ImageView>(R.id.cropped_image).setImageURI(croppedImageUri)
        val intent = Intent()
        Log.i("aaaa","0")
        sharebtn1.setOnClickListener(){
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_STREAM,finalUri)
            intent.type = "image/*"
            startActivity(Intent.createChooser(intent,"Please choose app"))
        }

    }
}


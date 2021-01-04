package com.example.viewpager2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_image_cropped.*
import java.security.AccessController.getContext

class CroppedImageActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_cropped)
        supportActionBar?.hide()

        var bundle: Bundle? = intent.getExtras()
        val croppedImageUri: Uri = bundle?.get("uri") as Uri
        findViewById<ImageView>(R.id.cropped_image).setImageURI(croppedImageUri)
        val intent = Intent()
        Log.i("aaaa","0")
        sharebtn1.setOnClickListener(){
            Log.i("aaaa","1")
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_STREAM,croppedImageUri)
            intent.type = "image/*"
            Log.i("aaaa","2")
            startActivity(Intent.createChooser(intent,"Please choose app"))
            Log.i("aaaa","3")
        }
//        val message : String = text.text.toString()
//        val intent = Intent()
//        intent.action = Intent.ACTION_SEND
//        intent.putExtra(Intent.EXTRA_TEXT, message)
//        intent.type = "text/plain"
//        startActivity(Intent.createChooser(intent, "Please select app "))

    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        val intent = Intent()
//        Log.i("aaaa","0")
//        sharebtn1.setOnClickListener(){
//            Log.i("aaaa","1")
//            intent.action = Intent.ACTION_SEND
//            intent.putExtra(Intent.EXTRA_STREAM,croppedImageUri)
//            intent.type = "image/*"
//            Log.i("aaaa","2")
//            startActivity(Intent.createChooser(intent,"Please choose app"))
//            Log.i("aaaa","3")
//        }
//    }

}


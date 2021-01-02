package com.example.viewpager2

import android.R.attr
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.File


class CropImageActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crop_layout)
        var originView = findViewById<ImageView>(R.id.originImage)
        var textView = findViewById<TextView>(R.id.title)
        var bundle: Bundle? = intent.getExtras()
        if(bundle != null){
            var name = intent.getStringExtra("name")
            var image = intent.getIntExtra("image",0)
            Log.i("aaaaa","before set")
            textView.setText(name)
            originView.setImageResource(image)
//            if(image!=null){
//                Log.i("aaaa",image.toString())
//            }
        }
//        originView.image = image
//        cropbtn.setOnClickListener{
//            CropImage.activity()
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .start(this)
//        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
//            var result = CropImage.getActivityResult(data)
//            crop.setImageURI(result.uri)
//        }
//    }
//    fun beginCrop(source: Uri) {
//        val destination: Uri = Uri.fromFile( File(getCacheDir(), "cropped"));
//        Crop.of(source, destination).asSquare().start(this);
//    }

//    fun handleCrop(resultCode: Int,  result: Intent) {
//        if (resultCode == RESULT_OK) {
//            resultView?.setImageURI(Crop.getOutput(result));
//        }
//    }


}
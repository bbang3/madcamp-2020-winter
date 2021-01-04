package com.example.viewpager2

import android.R.attr
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
//import com.example.viewpager2.ImageDetailAdapter.ImageDetailViewHolder.ImageDetailAdapter.Companion.crop
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.crop_layout.*
import kotlinx.android.synthetic.main.fragment_home.*



class CropImageActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crop_layout)
        var bundle: Bundle? = intent.getExtras()



        if(bundle != null){
            var image = intent.getIntExtra(ImageDetailAdapter.crop,0)
            var uri: Uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(image) + '/' + resources.getResourceTypeName(image) + '/' + resources.getResourceEntryName(image) )
            val bitmap: Bitmap = BitmapFactory.decodeResource(resources, image)
            Log.i("aaaa","cropimageview1")
            cropImageView.setImageBitmap(bitmap)
            Log.i("aaaa","cropimageview2")
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var image = intent.getIntExtra(ImageDetailAdapter.crop,0)
        var uri: Uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+resources.getResourcePackageName(image) + '/'+resources.getResourceTypeName(image)+'/'
                +resources.getResourceEntryName(image))
        launchImageCrop(uri)
        var result = CropImage.getActivityResult(data)
        if(resultCode == Activity.RESULT_OK){
            setImage(result.uri)
        }
        else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
            Log.i("aaaa","crop error")
        }
    }
    private fun setImage(uri: Uri?){
        var image1 : ImageView = findViewById(R.id.image)
        Glide.with(this)
            .load(uri)
            .into(image1)

    }

    private fun launchImageCrop(uri: Uri){
        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1920,1080)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .start(this)
    }




}
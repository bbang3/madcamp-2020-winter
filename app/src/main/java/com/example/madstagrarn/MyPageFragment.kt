package com.example.madstagrarn

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.madstagrarn.network.DataService
import retrofit2.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.*

class MyPageFragment: Fragment() {
    private val dataService: DataService = DataService()
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = arguments?.getSerializable("User") as User
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.mypage_fragment, container, false)

        val bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.madstagrarn_logo)
        val imageFile = convertBitmapToFile("sample.jpg", bitmapImage)
//        Log.i("hello", imageFile.extension)
        val reqFile = imageFile.asRequestBody("image/${imageFile.extension}".toMediaTypeOrNull())
//        Log.i("hello", "image/${imageFile.extension}")
//        Log.i("hello", "image/jpg".toMediaTypeOrNull().toString())
//        Log.i("hello!", reqFile.contentType().toString())
        val reqMultipartBody = MultipartBody.Part.createFormData("image", imageFile.name, reqFile)

        dataService.service.uploadPost(reqMultipartBody, "!!!").enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    Toast.makeText(context!!, "Success", Toast.LENGTH_SHORT)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }

        })
        return view
    }

    private fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        //create a file to write bitmap data
        val file = File(context?.cacheDir, fileName)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

}
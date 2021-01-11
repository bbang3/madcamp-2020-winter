package com.example.madstagrarn.mypage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madstagrarn.Post
import com.example.madstagrarn.R
import com.example.madstagrarn.User
import com.example.madstagrarn.network.DataService
import retrofit2.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.*

class MyPageFragment: Fragment() {
    private val dataService: DataService = DataService()
    private lateinit var currentUser: User

    private var postList: ArrayList<Post> = ArrayList()
    private var adapter: PostAdapter = PostAdapter(postList, dataService)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = arguments?.getSerializable("User") as User

        loadUserPostsFromServer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.mypage_fragment, container, false)

        view.findViewById<TextView>(R.id.mypage_name).text = currentUser.name
        view.findViewById<TextView>(R.id.mypage_phone).text = currentUser.phoneNumber
        view.findViewById<ImageView>(R.id.mypage_profile_image).setImageResource(R.drawable.person)

        val rvPost = view.findViewById<RecyclerView>(R.id.rv_mypost)
        rvPost.setHasFixedSize(true)
        rvPost.layoutManager = LinearLayoutManager(view.context)
        rvPost.adapter = adapter

//        val bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.madstagrarn_logo)
//        val imageFile = convertBitmapToFile("sample.jpg", bitmapImage)
//        val reqFile = imageFile.asRequestBody("image/${imageFile.extension}".toMediaTypeOrNull())
//        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, reqFile)
//        val userIdPart = currentUser.userId.toRequestBody(MultipartBody.FORM)
//        val descriptionPart = "My first post".toRequestBody(MultipartBody.FORM)
//
//        dataService.service.uploadPost(userIdPart, imagePart, descriptionPart).enqueue(object: Callback<Post>{
//            override fun onResponse(call: Call<Post>, response: Response<Post>) {
//                if(response.isSuccessful) {
//                    Log.i("uploadPost", response.body()!!.toString())
//                    Toast.makeText(context!!, "Success", Toast.LENGTH_SHORT)
//                }
//            }
//
//            override fun onFailure(call: Call<Post>, t: Throwable) {
//                t.printStackTrace()
//            }
//
//        })
        return view
    }

    private fun loadUserPostsFromServer() {
        dataService.service.getUserPosts(currentUser.userId).enqueue(object: Callback<ArrayList<Post>>{
            override fun onResponse(
                call: Call<ArrayList<Post>>,
                response: Response<ArrayList<Post>>
            ) {
                if(response.isSuccessful) {
                    postList.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()

                    Log.i("loadUserPosts", "${postList.size}")
                } else {
                    Toast.makeText(context!!, "Failed to load posts", Toast.LENGTH_SHORT)
                }
            }

            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                t.printStackTrace()
            }

        })
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
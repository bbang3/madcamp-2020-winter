package com.example.madstagrarn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madstagrarn.dataclass.Post
import com.example.madstagrarn.dataclass.User
import com.example.madstagrarn.mypage.PostAdapter
import com.example.madstagrarn.network.DataService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewActivity : AppCompatActivity() {
    private lateinit var currentUser: User
    private var dataService: DataService = DataService()

    private var postList: ArrayList<Post> = ArrayList()
    private lateinit var adapter: ViewPostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_view)

        val actionBar: ActionBar? = getSupportActionBar()
        actionBar?.hide()

        currentUser = intent.extras!!.get("User") as User
        Log.i("UserViewActivity", currentUser.toString())
        adapter = ViewPostAdapter(postList, currentUser, dataService)

        val rvView: RecyclerView = findViewById(R.id.rv_view)
        rvView.setHasFixedSize(true)
        rvView.layoutManager = LinearLayoutManager(this)
        rvView.adapter = adapter

        findViewById<TextView>(R.id.view_name).text = currentUser.name
        findViewById<TextView>(R.id.view_phone).text = currentUser.phoneNumber
        val profileImageView: ImageView = findViewById(R.id.view_profile_image)
        if(currentUser.profileImage.isNullOrEmpty() || currentUser.profileImage == "default_user_profile.png") {
            Glide.with(this)
                .load(R.drawable.profile)
                .circleCrop()
                .placeholder(R.drawable.loading_spinner)
                .into(profileImageView)
        } else {
            Glide.with(this)
                .load(dataService.BASE_URL + "image/${currentUser.profileImage}")
                .circleCrop()
                .placeholder(R.drawable.loading_spinner)
                .into(profileImageView)
        }

        loadUserPosts()
    }

    private fun loadUserPosts() {
        dataService.service.getUserPosts(currentUser.userId).enqueue(object:
            Callback<ArrayList<Post>> {
            override fun onResponse(
                call: Call<ArrayList<Post>>,
                response: Response<ArrayList<Post>>
            ) {
                if(response.isSuccessful) {
                    postList.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()

                    Log.i("loadUserPosts", "${postList.size}")
                } else {
                    Toast.makeText(this@UserViewActivity, "Failed to load posts", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
}
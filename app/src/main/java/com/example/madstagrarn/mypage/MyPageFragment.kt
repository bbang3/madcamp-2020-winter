package com.example.madstagrarn.mypage

import android.app.Activity.RESULT_OK
import android.content.Intent
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
import com.bumptech.glide.Glide
import com.example.madstagrarn.Post
import com.example.madstagrarn.R
import com.example.madstagrarn.User
import com.example.madstagrarn.network.DataService
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class MyPageFragment: Fragment() {
    private val dataService: DataService = DataService()
    private lateinit var currentUser: User

    private var postList: ArrayList<Post> = ArrayList()
    private var adapter: PostAdapter = PostAdapter(postList, dataService)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = arguments?.getSerializable("User") as User

        loadUserPosts()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.mypage_fragment, container, false)

        view.findViewById<TextView>(R.id.mypage_name).text = currentUser.name
        view.findViewById<TextView>(R.id.mypage_phone).text = currentUser.phoneNumber

        val profileImageView: ImageView = view.findViewById(R.id.mypage_profile_image)
        Glide.with(view)
            .load(dataService.BASE_URL + "image/default_user_profile.png")
            .circleCrop()
            .into(profileImageView)

        val addPostTextView: TextView = view.findViewById(R.id.add_post_text)
        addPostTextView.setOnClickListener {
            val intent = Intent(view.context, PostAddActivity::class.java)
            intent.putExtra("User", currentUser)
            startActivityForResult(intent, 0)
        }

        val rvPost = view.findViewById<RecyclerView>(R.id.rv_mypost)
        rvPost.setHasFixedSize(true)
        rvPost.layoutManager = LinearLayoutManager(view.context)
        rvPost.adapter = adapter


        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0) {
            if(resultCode == RESULT_OK) {
                val postId = data?.getStringExtra("postId")!!
                Log.i("loadNewPost", postId)
                loadNewPost(postId)
            }
        }
    }

    private fun loadUserPosts() {
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

    private fun loadNewPost(postId: String) {
        dataService.service.getPost(postId).enqueue(object: Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                Log.i("loadNewPost", response.body()!!.toString())
                if(response.isSuccessful) {
                    postList.add(0, response.body()!!)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }


}
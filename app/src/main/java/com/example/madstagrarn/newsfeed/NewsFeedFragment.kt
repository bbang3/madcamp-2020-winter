package com.example.madstagrarn.newsfeed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.madstagrarn.Post
import com.example.madstagrarn.R
import com.example.madstagrarn.User
import com.example.madstagrarn.network.DataService
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFeedFragment: Fragment() {
    private val dataService: DataService = DataService()
    private lateinit var currentUser: User

    private var postList: ArrayList<Post> = ArrayList()
    private var adapter: NewsFeedPostAdapter = NewsFeedPostAdapter(postList, dataService)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = arguments?.getSerializable("User") as User
        loadNewsFeedPosts()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.newsfeed_fragment, container, false)
        return view
    }

    private fun loadNewsFeedPosts() {
        dataService.service.getNewsFeedPosts(currentUser.userId).enqueue(object: Callback<ArrayList<Post>>{
            override fun onResponse(
                call: Call<ArrayList<Post>>,
                response: Response<ArrayList<Post>>
            ) {
                if(response.isSuccessful) {
                    postList.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context!!, "Failed to load posts", Toast.LENGTH_SHORT)
                }
            }

            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

}
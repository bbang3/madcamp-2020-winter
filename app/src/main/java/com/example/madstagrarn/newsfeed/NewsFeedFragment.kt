package com.example.madstagrarn.newsfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.madstagrarn.R
import com.example.madstagrarn.dataclass.Post
import com.example.madstagrarn.dataclass.User
import com.example.madstagrarn.network.DataService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFeedFragment: Fragment() {
    private val dataService: DataService = DataService()
    private lateinit var currentUser: User
    private val postList: ArrayList<Post> = ArrayList()
    private var adapter: NewsFeedPostAdapter = NewsFeedPostAdapter(postList, dataService)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = arguments?.getSerializable("User") as User
        loadNewsFeedPosts()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.newsfeed_fragment, container, false)

        val rvPosts: RecyclerView = view.findViewById(R.id.rv_newsfeed_posts)
        rvPosts.setHasFixedSize(true)
        rvPosts.adapter = adapter
        rvPosts.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)

        val swipeRefresh: SwipeRefreshLayout = view.findViewById(R.id.swipe_fresh)
        swipeRefresh.setOnRefreshListener {
            refreshNewsFeed(swipeRefresh)
        }

        return view
    }

    private fun refreshNewsFeed(swipeRefresh: SwipeRefreshLayout) {
        swipeRefresh.isRefreshing = true
        dataService.service.getNewsFeedPosts(currentUser.userId).enqueue(object: Callback<ArrayList<Post>>{
            override fun onResponse(
                call: Call<ArrayList<Post>>,
                response: Response<ArrayList<Post>>
            ) {
                if(response.isSuccessful) {
                    val receivedPostList: ArrayList<Post> = response.body()!!
                    if(receivedPostList.size > postList.size) { // refresh only when new posts are loaded
                        for(i in 0 until (receivedPostList.size - postList.size))
                            postList.add(i, receivedPostList[i])
                        adapter.notifyDataSetChanged()
                    }
                }
                swipeRefresh.isRefreshing = false
            }
            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                t.printStackTrace()
                swipeRefresh.isRefreshing = false
            }
        })
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
                }
            }

            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
}
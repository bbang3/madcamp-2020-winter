package com.example.madstagram

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madstagram.network.DataService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment() {
    private val dataService: DataService = DataService()
    private var followingUserList: ArrayList<User> = ArrayList()
    private var currentUserId: String = "5ff9d8a656a88c7127c00685"
    private lateinit var currentUser: User
    private lateinit var adapter: FollowingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_following, container, false)
        loadCurrentUserInfo()

        val rvFollowing: RecyclerView = view.findViewById(R.id.rv_following)
        rvFollowing.setHasFixedSize(true)
        adapter = FollowingAdapter(followingUserList)
        rvFollowing.adapter = adapter
        rvFollowing.layoutManager = LinearLayoutManager(view.context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadCurrentUserInfo() {
        dataService.service.getUser(currentUserId).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful) {
                    Log.i("loadCurrentUserInfo", response.body()!!.toString())
                    currentUser = response.body()!!
                    loadFollowingUserList()
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun loadFollowingUserList() {
        dataService.service.getFollowingUsers(currentUser._id).enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if(response.isSuccessful){
                    Log.i("loadCurrentUserInfo", response.body()!!.toString())
                    followingUserList.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
}
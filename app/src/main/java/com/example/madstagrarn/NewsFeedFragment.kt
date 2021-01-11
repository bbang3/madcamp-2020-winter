package com.example.madstagrarn

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.madstagrarn.network.DataService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFeedFragment: Fragment() {
    private val dataService: DataService = DataService()
    private lateinit var currentUser: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // User 정보는 arguments?.getSerializable("User") 에 들어있음!

        currentUser = arguments?.getSerializable("User") as User

        val view: View = inflater.inflate(R.layout.newsfeed_fragment, container, false)
        return view
    }

    private fun loadCurrentUserInfo() {
        dataService.service.getUser(currentUser._id).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful) {
                    Log.i("loadCurrentUserInfo", response.body()!!.toString())
                    currentUser = response.body()!!
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}
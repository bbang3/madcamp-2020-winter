package com.example.madstagrarn.following

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madstagrarn.R
import com.example.madstagrarn.dataclass.User
import com.example.madstagrarn.network.DataService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment() {
    private val dataService: DataService =DataService()
    private var followingUserList: ArrayList<User> = ArrayList()
    private lateinit var currentUser: User
    private lateinit var adapter: FollowingAdapter
    private lateinit var rvFollowing: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = arguments?.getSerializable("User") as User

        loadCurrentUserInfo()
        loadFollowingUserList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // User 정보는 arguments?.getSerializable("User") 에 들어있음!
        loadCurrentUserInfo()
        loadFollowingUserList()

        val view = inflater.inflate(R.layout.following_fragment, container, false)
        Log.i("onCreateView", "Created")

        rvFollowing = view.findViewById(R.id.rv_following)
        rvFollowing.setHasFixedSize(true)
        adapter = FollowingAdapter(
            followingUserList,
            currentUser
        )
        rvFollowing.adapter = adapter
        rvFollowing.layoutManager = LinearLayoutManager(view.context)

        val followingAddButton: FloatingActionButton = view.findViewById(R.id.following_add_button)
        followingAddButton.setOnClickListener {
            val intent = Intent(activity, FollowingAddActivity::class.java)
            intent.putExtra("User", currentUser)
            startActivityForResult(intent, 0)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        loadCurrentUserInfo()
        loadFollowingUserList()
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

    private fun loadFollowingUserList() {
        dataService.service.getFollowingUsers(currentUser._id).enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if(response.isSuccessful){
                    followingUserList.clear()
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
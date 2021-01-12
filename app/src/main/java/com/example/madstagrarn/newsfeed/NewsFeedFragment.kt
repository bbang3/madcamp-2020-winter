package com.example.madstagrarn.newsfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.madstagrarn.R
import com.example.madstagrarn.dataclass.User
import com.example.madstagrarn.network.DataService

class NewsFeedFragment: Fragment() {
    private val dataService: DataService = DataService()
    private lateinit var currentUser: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // User 정보는 arguments?.getSerializable("User") 에 들어있음!

        currentUser = arguments?.getSerializable("User") as User

        val view: View = inflater.inflate(R.layout.newsfeed_fragment, container, false)
        return view
    }
}
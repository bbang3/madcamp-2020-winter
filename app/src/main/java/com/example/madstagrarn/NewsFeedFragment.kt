package com.example.madstagrarn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class NewsFeedFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // User 정보는 arguments?.getSerializable("User") 에 들어있음!

        val view: View = inflater.inflate(R.layout.newsfeed_fragment, container, false)
        return view
    }
}
package com.example.madstagrarn

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.madstagrarn.following.FollowingFragment
import com.example.madstagrarn.mypage.MyPageFragment
import com.example.madstagrarn.newsfeed.NewsFeedFragment

class TabViewPagerAdapter(fm: FragmentManager, val bundle: Bundle): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    private var fragmentList = listOf(
        Pair("Following", FollowingFragment()), Pair("NewsFeed",
            NewsFeedFragment()
        ), Pair("MyPage", MyPageFragment())
    )

    override fun getItem(position: Int): Fragment {
        val fragment: Fragment = fragmentList[position].second
        fragment.arguments = bundle
        return fragment
    }

    override fun getCount(): Int = fragmentList.size

//    override fun getPageTitle(position: Int): CharSequence? {
//        return fragmentList[position].first
//    }
}
package com.example.madstagrarn

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.madstagrarn.mypage.MyPageFragment

class TabViewPagerAdapter(fm: FragmentManager, bundle: Bundle): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    private val adapterBundle: Bundle = bundle
    private var fragmentList = listOf(
        Pair("Following", FollowingFragment()), Pair("NewsFeed", NewsFeedFragment()), Pair("MyPage", MyPageFragment())
    )

    override fun getItem(position: Int): Fragment {
        val fragment: Fragment = fragmentList[position].second
        fragment.arguments = adapterBundle
        return fragment
    }

    override fun getCount(): Int = fragmentList.size

//    override fun getPageTitle(position: Int): CharSequence? {
//        return fragmentList[position].first
//    }
}
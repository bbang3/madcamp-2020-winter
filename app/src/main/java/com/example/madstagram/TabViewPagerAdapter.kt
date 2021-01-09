package com.example.madstagram

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    private var fragmentList = listOf(
        Pair("Following", Following()), Pair("NewsFeed", NewsFeed()), Pair("MyPage", MyPage())
    )

    override fun getItem(position: Int): Fragment {
        return fragmentList[position].second
    }

    override fun getCount(): Int = fragmentList.size

//    override fun getPageTitle(position: Int): CharSequence? {
//        return fragmentList[position].first
//    }
}
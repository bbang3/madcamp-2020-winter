package com.example.madstagram

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tabs: TabLayout = findViewById(R.id.tabs)
        val tabViewPager: ViewPager = findViewById(R.id.tab_view_pager);
        val tabViewPagerAdapter = TabViewPagerAdapter(supportFragmentManager)
        tabViewPager.adapter = tabViewPagerAdapter
        tabs.setupWithViewPager(tabViewPager)

        tabViewPager.setCurrentItem(1)
        tabs.getTabAt(0)!!.setIcon(R.drawable.search)
        tabs.getTabAt(1)!!.setIcon(R.drawable.home)
        tabs.getTabAt(2)!!.setIcon(R.drawable.person)
    }
}
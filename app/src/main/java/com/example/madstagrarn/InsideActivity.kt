package com.example.madstagrarn

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import java.io.Serializable


class InsideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inside)

        val bundle = Bundle()
        bundle.putSerializable("User", intent.extras!!.get("User") as Serializable)

        val tabs: TabLayout = findViewById(R.id.tabs)
        val tabViewPager: ViewPager = findViewById(R.id.tab_view_pager);
        val tabViewPagerAdapter = TabViewPagerAdapter(supportFragmentManager, bundle)
        tabViewPager.adapter = tabViewPagerAdapter
        tabs.setupWithViewPager(tabViewPager)

        tabViewPager.setCurrentItem(1)
        tabs.getTabAt(0)!!.setIcon(R.drawable.search)
        tabs.getTabAt(1)!!.setIcon(R.drawable.home)
        tabs.getTabAt(2)!!.setIcon(R.drawable.person)
    }

    override fun onBackPressed() {
        this.finish()
    }
}
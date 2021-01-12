package com.example.madstagrarn

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.madstagrarn.dataclass.User
import com.example.madstagrarn.network.DataService
import com.google.android.material.tabs.TabLayout


class InsideActivity : AppCompatActivity() {
    private val dataService: DataService = DataService()
    lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inside)
        val actionBar: ActionBar? = getSupportActionBar()
        actionBar?.hide()

        val bundle = Bundle()
        currentUser = intent.extras!!.get("User") as User
        bundle.putSerializable("User", currentUser)

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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
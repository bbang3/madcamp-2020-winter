package com.example.madstagrarn

import android.app.Notification
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.madstagrarn.network.DataService
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable


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

        val logoutButton = findViewById<ImageView>(R.id.logout_button)
        logoutButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

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
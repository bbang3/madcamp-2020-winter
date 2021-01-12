package com.example.madstagrarn

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.madstagrarn.dataclass.User
import com.example.madstagrarn.following.FollowingFragment
import com.example.madstagrarn.mypage.MyPageFragment
import com.example.madstagrarn.network.DataService
import com.example.madstagrarn.newsfeed.NewsFeedFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class InsideActivity : AppCompatActivity() {
    private val dataService: DataService = DataService()
    lateinit var currentUser: User

    private val fragmentOne = FollowingFragment()
    private val fragmentTwo = NewsFeedFragment()
    private val fragmentThree = MyPageFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inside)

        val bundle = Bundle()
        currentUser = intent.extras!!.get("User") as User
        bundle.putSerializable("User", currentUser)

        fragmentOne.arguments = bundle
        fragmentTwo.arguments = bundle
        fragmentThree.arguments = bundle

        replaceFragment(fragmentTwo)

        findViewById<BottomNavigationView>(R.id.bottom_navbar).setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_following -> {
                    replaceFragment(fragmentOne)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_home -> {
                    replaceFragment(fragmentTwo)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_mypage -> {
                    replaceFragment(fragmentThree)
                    return@setOnNavigationItemSelectedListener true
                }
                else ->
                    return@setOnNavigationItemSelectedListener false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frameLayout, fragment)
        ft.commit()
    }

    override fun onBackPressed() {

    }
}
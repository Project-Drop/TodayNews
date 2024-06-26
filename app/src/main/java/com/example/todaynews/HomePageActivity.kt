package com.example.todaynews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomePageActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.News -> {
                    replaceFragments(NewsFragment())
                    true
                }
                R.id.Profile -> {
                    replaceFragments(ProfileFragment())
                    true
                }

                R.id.Report -> {
                    replaceFragments(FeedbackFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragments(NewsFragment())
    }

    private fun replaceFragments(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
}
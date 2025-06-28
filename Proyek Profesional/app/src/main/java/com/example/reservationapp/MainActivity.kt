package com.example.reservationapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.reservationapp.fragment.Activity
import com.example.reservationapp.fragment.Booking
import com.example.reservationapp.fragment.Home
import com.example.reservationapp.fragment.Profile
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var btmMenu: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Set the initial fragment to Home
        loadFragment(Home())

        btmMenu = findViewById(R.id.bottomNav)

        // Handle bottom navigation item clicks
        btmMenu.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> loadFragment(Home())
                R.id.booking -> loadFragment(Booking())
                R.id.activiy -> loadFragment(Activity())
                R.id.profile -> loadFragment(Profile())
            }
            true
        }

        // Apply padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Function to load a fragment
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}

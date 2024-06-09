package com.android.haivest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.haivest.databinding.ActivityMainBinding
import com.android.haivest.ui.analyze.AnalyzeFragment
import com.android.haivest.ui.business.BusinessFragment
import com.android.haivest.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.background = null

        // Set the initial fragment
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        @Suppress("DEPRECATION")
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.navigation_analyze-> {
                    replaceFragment(AnalyzeFragment())
                    true
                }
                R.id.navigation_business -> {
                    replaceFragment(BusinessFragment())
                    true
                }

                else -> false
            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }
}
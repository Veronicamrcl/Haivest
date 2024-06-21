package com.android.haivest

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.android.haivest.data.factory.ViewModelFactory
import com.android.haivest.databinding.ActivityMainBinding
import com.android.haivest.ui.auth.login.LoginActivity
import com.android.haivest.ui.home.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val bottomNav: BottomNavigationView = binding.bottomNavigationView
            val navController = findNavController(R.id.nav_host_fragment)

            bottomNav.setupWithNavController(navController)
        }

        getSession()

    }

    private fun getSession() {

        viewModel.getSession().observe(this) {
            token = it.token
            if (!it.isLogin) {
                movesToAuth()
            }
        }
    }

    private fun movesToAuth() {
        Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
        }
    }

}
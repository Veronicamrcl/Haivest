package com.android.haivest.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.haivest.data.factory.ViewModelFactory
import com.android.haivest.databinding.ActivityProfileBinding
import com.android.haivest.ui.auth.login.LoginActivity

class ProfileActivity:AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnLogout.setOnClickListener {
            profileViewModel.logout()
            movesToAuth()
        }

        val sharedPref = this.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "")

        binding.profileName.text = username

    }

    private fun movesToAuth() {
        Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
        }
    }

}
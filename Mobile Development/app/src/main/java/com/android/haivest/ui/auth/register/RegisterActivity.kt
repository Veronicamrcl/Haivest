package com.android.haivest.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.haivest.MainActivity
import com.android.haivest.data.factory.ViewModelFactory
import com.android.haivest.databinding.ActivityRegisterBinding
import com.android.haivest.ui.auth.login.LoginActivity

class RegisterActivity:AppCompatActivity() {

    private lateinit var binding:ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                registerViewModel.register(name, username,password)
                binding.progressBar.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show()
            }

        }

        registerViewModel.registrationResult.observe(this){ result ->
            binding.progressBar.visibility = View.GONE

            if (result.success) {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("username", binding.usernameEditText.text.toString())
                startActivity(intent)
            } else {
                Toast.makeText(this, "Registration failed: ${result.error}", Toast.LENGTH_SHORT).show()
            }
        }


        binding.tvToLogin.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

}
package com.android.haivest.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.haivest.MainActivity
import com.android.haivest.data.factory.ViewModelFactory
import com.android.haivest.data.model.User
import com.android.haivest.databinding.ActivityLoginBinding
import com.android.haivest.ui.auth.register.RegisterActivity

class LoginActivity:AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvToRegister.setOnClickListener {
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.loginPasswordEditText.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.login(username, password)
                binding.progressBar.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.loginResult.observe(this) { result ->
            binding.progressBar.visibility = View.GONE
            if (result.success) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                result.data?.let { loginResponse ->
                    setupAction(loginResponse.accessToken)
                }
            } else {
                Toast.makeText(this, "Login failed: ${result.error}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupAction(token: String) {
        val email = binding.usernameEditText.text.toString()
        loginViewModel.saveSession(User(email, token))

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

}
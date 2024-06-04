package com.example.storyapp.ui.register

import android.os.Bundle
import com.example.storyapp.data.Result
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityRegisterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModel<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()
        setListeners()
    }

    private fun observeViewModel() {
        registerViewModel.apply {
            isLoading.observe(this@RegisterActivity, ::showLoading)
            errorMessage.observe(this@RegisterActivity, ::showToast)
        }
    }

    private fun setListeners() {
        binding.apply {
            btnRegister.setOnClickListener {
                if (edName.text.isNullOrEmpty()) {
                    showToast(ContextCompat.getString(this@RegisterActivity, R.string.error_name))
                    return@setOnClickListener
                }

                if (edEmail.error.isNullOrEmpty().not() || edEmail.text.isNullOrEmpty()) {
                    showToast(ContextCompat.getString(this@RegisterActivity, R.string.error_email))
                    return@setOnClickListener
                }

                if (edPassword.error.isNullOrEmpty().not() || edPassword.text.isNullOrEmpty()) {
                    showToast(
                        ContextCompat.getString(
                            this@RegisterActivity,
                            R.string.error_password
                        )
                    )
                    return@setOnClickListener
                }

                registerViewModel.apply {
                    register(
                        edName.text.toString(),
                        edEmail.text.toString(),
                        edPassword.text.toString()
                    )
                        .observe(this@RegisterActivity) { result ->
                            when (result) {
                                is Result.Loading -> isLoading.postValue(true)
                                is Result.Success -> {
                                    isLoading.postValue(false)
                                    errorMessage.postValue(result.data.message.toString())
                                    finish()
                                }

                                is Result.Error -> {
                                    isLoading.postValue(false)
                                    errorMessage.postValue(result.error)
                                }
                            }
                        }
                }
            }

            btnLogin.setOnClickListener {
                finish()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressbar.isVisible = isLoading
            btnRegister.isVisible = !isLoading
            edName.isEnabled = !isLoading
            edEmail.isEnabled = !isLoading
            edPassword.isEnabled = !isLoading
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
package com.android.haivest.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.haivest.MainActivity
import com.android.haivest.R
import com.android.haivest.data.factory.ViewModelFactory
import com.android.haivest.ui.auth.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity:AppCompatActivity() {

    private val viewModel by viewModels<SplashViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val DURATION_TIME = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            viewModel.getSession().observe(this) { user ->
                if (user.isLogin) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                finish()
            }
        }, DURATION_TIME)



    }

}
package com.guestkeeper.pro.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.guestkeeper.pro.R
import com.guestkeeper.pro.utils.PreferenceManager

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_DELAY = 1500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            checkUserSession()
        }, SPLASH_DELAY)
    }

    private fun checkUserSession() {
        val preferenceManager = PreferenceManager(this)
        val isLoggedIn = preferenceManager.isLoggedIn()
        val sessionValid = preferenceManager.isSessionValid()

        val intent = if (isLoggedIn && sessionValid) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}


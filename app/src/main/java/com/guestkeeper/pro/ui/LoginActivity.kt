package com.guestkeeper.pro.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.guestkeeper.pro.database.AppDatabase
import com.guestkeeper.pro.databinding.ActivityLoginBinding
import com.guestkeeper.pro.utils.PreferenceManager
import com.guestkeeper.pro.utils.SecurityUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: AppDatabase
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        preferenceManager = PreferenceManager(this)

        setupUI()
    }

    private fun setupUI() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (validateInput(email, password)) {
                loginUser(email, password)
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }

        binding.tvSkipDemo.setOnClickListener {
            startDemoMode()
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            return false
        }

        if (!SecurityUtils.isValidEmail(email)) {
            binding.etEmail.error = "Invalid email format"
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            return false
        }

        return true
    }

    private fun loginUser(email: String, password: String) {
        lifecycleScope.launch {
            binding.progressBar.visibility = android.view.View.VISIBLE
            binding.btnLogin.isEnabled = false

            try {
                val user = withContext(Dispatchers.IO) {
                    database.userDao().getByEmail(email)
                }

                if (user == null) {
                    showError("User not found")
                    return@launch
                }

                if (!user.isActive) {
                    showError("Account is deactivated")
                    return@launch
                }

                val passwordHash = SecurityUtils.hashPassword(password)
                if (user.passwordHash != passwordHash) {
                    showError("Invalid password")
                    return@launch
                }

                // Update last login
                withContext(Dispatchers.IO) {
                    database.userDao().updateLastLogin(user.id, System.currentTimeMillis())
                }

                // Save session
                preferenceManager.saveUserSession(
                    userId = user.id,
                    email = user.email,
                    role = user.role.name,
                    fullName = user.fullName ?: ""
                )

                // Navigate to main activity
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()

            } catch (e: Exception) {
                showError("Login failed: ${e.message}")
            } finally {
                binding.progressBar.visibility = android.view.View.GONE
                binding.btnLogin.isEnabled = true
            }
        }
    }

    private fun showForgotPasswordDialog() {
        // TODO: Implement forgot password dialog
        Toast.makeText(this, "Forgot password feature coming soon", Toast.LENGTH_SHORT).show()
    }

    private fun startDemoMode() {
        preferenceManager.saveDemoMode(true)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}


package com.example.utucanteenapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.utucanteenapp.databinding.ActivityLoginBinding
import com.example.utucanteenapp.models.User
import com.example.utucanteenapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Login Button Click
        binding.Loginbutton.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate to SignActivity
        binding.donthavebutton.setOnClickListener {
            startActivity(Intent(this, SignActivity::class.java))
            finish()
        }
    }

    private fun loginUser(email: String, password: String) {
        val loginUser = User("", "", email, password) // Enrollment & Phone not needed for login

        RetrofitClient.instance.loginUser(loginUser).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("API_SUCCESS", "User logged in successfully!")
                    Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Log.e("API_ERROR", "Login failed")
                    Toast.makeText(this@LoginActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API_FAILURE", "Error: ${t.message}")
                Toast.makeText(this@LoginActivity, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

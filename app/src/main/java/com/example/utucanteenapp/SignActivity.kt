package com.example.utucanteenapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.utucanteenapp.databinding.ActivitySignBinding
import com.example.utucanteenapp.models.User
import com.example.utucanteenapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignActivity : AppCompatActivity() {
    private val binding: ActivitySignBinding by lazy {
        ActivitySignBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Sign Up Button Click
        binding.Loginbutton.setOnClickListener {
            val enrollment = binding.editTextEnrollment.text.toString().trim()
            val phone = binding.editTextPhoneNumber.text.toString().trim()
            val email = binding.editTextTextEmailAddress.text.toString().trim()
            val password = binding.editTextTextPassword.text.toString().trim()

            if (validateInput(enrollment, phone, email, password)) {
                registerUser(enrollment, phone, email, password)
            }
        }

        // Navigate to LoginActivity
        binding.alreadyhavebutton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateInput(enrollment: String, phone: String, email: String, password: String): Boolean {
        // Validate Enrollment Number (Exactly 6 digits)
        if (enrollment.length != 15 || !enrollment.all { it.isDigit() }) {
            Toast.makeText(this, "Invalid Enrollment Number (Must be 15 digits)", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validate Phone Number (Exactly 10 digits)
        if (phone.length != 10 || !phone.all { it.isDigit() }) {
            Toast.makeText(this, "Invalid Phone Number (Must be 10 digits)", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validate Email Format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validate Password Strength
        if (!isValidPassword(password)) {
            Toast.makeText(
                this,
                "Password must be at least 8 characters, include an uppercase letter, a lowercase letter, a digit, and a special character.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        return true
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!]).{6,}$"
        return password.matches(passwordPattern.toRegex())
    }

    private fun registerUser(enrollment: String, phone: String, email: String, password: String) {
        val newUser = User(enrollment, phone, email, password)

        RetrofitClient.instance.registerUser(newUser).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("API_SUCCESS", "User registered successfully!")
                    Toast.makeText(this@SignActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SignActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Log.e("API_ERROR", "Registration failed")
                    Toast.makeText(this@SignActivity, "Registration Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API_FAILURE", "Error: ${t.message}")
                Toast.makeText(this@SignActivity, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

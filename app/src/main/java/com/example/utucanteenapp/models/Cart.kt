package com.example.utucanteenapp.models

data class Cart(
    val id: String,
    val userId: String,
    val productId: String,
    val quantity: Int
)

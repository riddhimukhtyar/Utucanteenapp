package com.example.utucanteenapp.network

import com.example.utucanteenapp.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("register")
    fun registerUser(@Body user: User): Call<Void>

    @POST("login")
    fun loginUser(@Body user: User): Call<Void>

    @POST("reset-password")
    fun forgotPassword(@Query("email") email: String): Call<Void>

    @POST("changepassword")
    fun changePassword(
        @Query("email") email: String,
        @Query("oldPassword") oldPassword: String,
        @Query("newPassword") newPassword: String
    ): Call<Void>

    @GET("listProduct")
    fun getProducts(): Call<List<product>>

    @POST("addToCart")
    fun addToCart(@Query("productId") productId: String): Call<Void>

    @GET("getCart")
    fun getCart(): Call<List<product>>
}

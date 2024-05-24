package com.lolos.asn.data.retrofit

import android.util.Log
import com.lolos.asn.data.response.LoginRequest
import com.lolos.asn.data.response.LoginResponse
import com.lolos.asn.data.response.RegisterRequest
import com.lolos.asn.data.response.RegisterResponse
import com.lolos.asn.data.response.TestResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/")
    fun fetchURL(): Call<TestResponse>

    @POST("login")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @POST("accounts")
    fun register(
        @Body request: RegisterRequest
    ): Call<RegisterResponse>
}
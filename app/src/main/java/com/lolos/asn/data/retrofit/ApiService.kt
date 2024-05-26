package com.lolos.asn.data.retrofit

import com.lolos.asn.data.response.CourseResponse
import com.lolos.asn.data.response.LoginRequest
import com.lolos.asn.data.response.LoginResponse
import com.lolos.asn.data.response.RegisterRequest
import com.lolos.asn.data.response.RegisterResponse
import com.lolos.asn.data.response.TestResponse
import com.lolos.asn.data.response.TryoutResponse
import com.lolos.asn.data.response.UserDataResponse
import com.lolos.asn.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET("accounts/{account_id}")
    fun getAuthUserData(
        @Path("account_id") userId: String?
    ): Call<UserResponse>

    @GET("tryouts/newest")
    fun getNewestTryout(): Call<TryoutResponse>

    @GET("courses/account/{account_id}/category/{category_id}")
    fun getCourses(
        @Path("account_id") userId: String?,
        @Path("category_id") categoryId: String
    ): Call<CourseResponse>

}
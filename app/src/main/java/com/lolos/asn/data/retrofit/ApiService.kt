package com.lolos.asn.data.retrofit

import com.lolos.asn.data.response.CourseDetailResponse
import com.lolos.asn.data.response.CourseResponse
import com.lolos.asn.data.response.ExaminationResponse
import com.lolos.asn.data.response.LeaderboardResponse
import com.lolos.asn.data.response.LoginRequest
import com.lolos.asn.data.response.LoginResponse
import com.lolos.asn.data.response.RegisterRequest
import com.lolos.asn.data.response.RegisterResponse
import com.lolos.asn.data.response.TestResponse
import com.lolos.asn.data.response.TryoutDetailResponse
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

    @GET("courses/account/{account_id}/category/{category_id}")
    fun getCourses(
        @Path("account_id") userId: String?,
        @Path("category_id") categoryId: String
    ): Call<CourseResponse>

    @GET("courses/{course_id}/account/{account_id}")
    fun getDetailCourse(
        @Path("course_id") courseId: String?,
        @Path("account_id") userId: String?
    ): Call<CourseDetailResponse>

    @GET("tryouts/{tryout_id}/account/{account_id}")
    fun getDetailTryout(
        @Path("tryout_id") tryoutId: String?,
        @Path("account_id") userId: String?
    ): Call<TryoutDetailResponse>

    @GET("tryouts/newest")
    fun getNewestTryout(): Call<TryoutResponse>

    @GET("tryouts/free")
    fun getFreeTryout(): Call<TryoutResponse>

    @GET("tryouts/pay")
    fun getPaidTryout(): Call<TryoutResponse>

    @GET("tryouts/{tryout_id}/start")
    fun startTryout(
        @Path("tryout_id") tryoutId: String?
    ): Call<ExaminationResponse>

    @GET("leaderboard/{tryout_id}")
    fun getTryoutLeaderboard(
        @Path("tryout_id") tryoutId: String?
    ): Call<LeaderboardResponse>

}
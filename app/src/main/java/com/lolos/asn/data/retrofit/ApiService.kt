package com.lolos.asn.data.retrofit

import com.lolos.asn.data.response.CourseDetailResponse
import com.lolos.asn.data.response.CourseResponse
import com.lolos.asn.data.response.ExaminationResponse
import com.lolos.asn.data.response.FinishTryoutResponse
import com.lolos.asn.data.response.FinishedTryoutResponse
import com.lolos.asn.data.response.LeaderboardResponse
import com.lolos.asn.data.response.LoginRequest
import com.lolos.asn.data.response.LoginResponse
import com.lolos.asn.data.response.NotificationResponse
import com.lolos.asn.data.response.PurchaseResponse
import com.lolos.asn.data.response.RegisterRequest
import com.lolos.asn.data.response.RegisterResponse
import com.lolos.asn.data.response.TransactionHistoryResponse
import com.lolos.asn.data.response.TryoutBundleDetailResponse
import com.lolos.asn.data.response.TryoutBundleResponse
import com.lolos.asn.data.response.TryoutDetailResponse
import com.lolos.asn.data.response.TryoutRequest
import com.lolos.asn.data.response.TryoutResponse
import com.lolos.asn.data.response.TryoutResultResponse
import com.lolos.asn.data.response.UpdateUserResponse
import com.lolos.asn.data.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

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

    @Multipart
    @PUT("accounts/{account_id}")
    fun updateUserData(
        @Path("account_id") userId: String?,
        @Part avatar: MultipartBody.Part?,
        @Part("name") name: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("password") password: RequestBody?,
        @Part("phone") phone: RequestBody?
    ): Call<UpdateUserResponse>

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

    @POST("clearedCourse/{account_id}/{course_id}")
    fun finishCourse(
        @Path("course_id") courseId: String?,
        @Path("account_id") userId: String?
    ): Call<CourseDetailResponse>

    @GET("tryouts/account/{account_id}")
    fun getAllTryouts(
        @Path("account_id") userId: String?
    ): Call<TryoutResponse>

    @GET("tryouts/{tryout_id}/account/{account_id}")
    fun getDetailTryout(
        @Path("tryout_id") tryoutId: String?,
        @Path("account_id") userId: String?
    ): Call<TryoutDetailResponse>

    @GET("tryouts/newest")
    fun getNewestTryout(): Call<TryoutResponse>

    @GET("freeTryouts/account/{account_id}")
    fun getFreeTryout(
        @Path("account_id") userId: String?
    ): Call<TryoutResponse>

    @GET("payTryouts/account/{account_id}")
    fun getPaidTryout(
        @Path("account_id") userId: String?
    ): Call<TryoutResponse>

    @GET("tryouts/{tryout_id}/start")
    fun startTryout(
        @Path("tryout_id") tryoutId: String?
    ): Call<ExaminationResponse>

    @GET("tryouts/stats/{tryout_id}/{account_id}")
    fun getTryoutResult(
        @Path("tryout_id") tryoutId: String?,
        @Path("account_id") userId: String?
    ): Call<TryoutResultResponse>

    @GET("tryouts/finished/{account_id}")
    fun getFinishedTryout(
        @Path("account_id") userId: String?
    ): Call<FinishedTryoutResponse>

    @GET("tryoutBundles/account/{account_id}")
    fun getBundle(
        @Path("account_id") userId: String?
    ): Call<TryoutBundleResponse>

    @GET("tryoutbundles/account/{account_id}/detail/{bundle_id}")
    fun getDetailBundle(
        @Path("account_id") userId: String?,
        @Path("bundle_id") bundleId: String?
    ): Call<TryoutBundleDetailResponse>

    @POST("tryouts/cleared/{tryout_id}/{account_id}")
    fun clearTryout(
        @Path("tryout_id") tryoutId: String?,
        @Path("account_id") userId: String?,
        @Body request: TryoutRequest
    ): Call<FinishTryoutResponse>

    @GET("leaderboard/{tryout_id}")
    fun getTryoutLeaderboard(
        @Path("tryout_id") tryoutId: String?
    ): Call<LeaderboardResponse>

    @Multipart
    @POST("tryouts/transaction/{account_id}")
    fun sendTransaction(
        @Path("account_id") userId: String?,
        @Part bukti_transaksi: MultipartBody.Part,
        @Part("transaction_title") transactionTitle: RequestBody,
        @Part("transaction_price") transactionPrice: RequestBody,
        @Part("listTryout") listTryout: RequestBody,
    ): Call<PurchaseResponse>

    @GET("transaction/history/{account_id}")
    fun getTransactionHistory(
        @Path("account_id") userId: String?
    ): Call<TransactionHistoryResponse>

    @GET("notifikasi/{account_id}")
    fun getNotification(
        @Path("account_id") userId: String?
    ): Call<NotificationResponse>

    @PUT("updateNotif/{account_id}/{notifikasi_id}")
    fun updateNotification(
        @Path("account_id") userId: String?,
        @Path("notifikasi_id") notificationId: String?
    ): Call<NotificationResponse>
}
package com.lolos.asn.data.retrofit

import com.lolos.asn.data.response.AnalysisResponse
import com.lolos.asn.data.response.CourseDetailResponse
import com.lolos.asn.data.response.CourseResponse
import com.lolos.asn.data.response.DrillingDetailResponse
import com.lolos.asn.data.response.DrillingFinishResponse
import com.lolos.asn.data.response.DrillingHistoryResponse
import com.lolos.asn.data.response.DrillingRequest
import com.lolos.asn.data.response.DrillingResponse
import com.lolos.asn.data.response.DrillingScoreDetailResponse
import com.lolos.asn.data.response.DrillingStartResponse
import com.lolos.asn.data.response.ExaminationResponse
import com.lolos.asn.data.response.FinishTryoutResponse
import com.lolos.asn.data.response.FinishedTryoutResponse
import com.lolos.asn.data.response.LeaderboardResponse
import com.lolos.asn.data.response.LoginRequest
import com.lolos.asn.data.response.LoginResponse
import com.lolos.asn.data.response.NewestArticleResponse
import com.lolos.asn.data.response.NotificationResponse
import com.lolos.asn.data.response.PopularArticleResponse
import com.lolos.asn.data.response.PurchaseResponse
import com.lolos.asn.data.response.RegisterRequest
import com.lolos.asn.data.response.RegisterResponse
import com.lolos.asn.data.response.TokenRequest
import com.lolos.asn.data.response.TokenResponse
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
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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
        @Path("account_id") userId: String?,
        @Header("Authorization") token: String
    ): Call<UserResponse>

    @Multipart
    @PUT("accounts/{account_id}")
    fun updateUserData(
        @Path("account_id") userId: String?,
        @Part avatar: MultipartBody.Part?,
        @Part("name") name: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("password") password: RequestBody?,
        @Part("phone") phone: RequestBody?,
        @Header("Authorization") token: String
    ): Call<UpdateUserResponse>

    @GET("courses/account/{account_id}/category/{category_id}")
    fun getCourses(
        @Path("account_id") userId: String?,
        @Path("category_id") categoryId: String,
        @Header("Authorization") token: String
    ): Call<CourseResponse>

    @GET("courses/{course_id}/account/{account_id}")
    fun getDetailCourse(
        @Path("course_id") courseId: String?,
        @Path("account_id") userId: String?,
        @Header("Authorization") token: String
    ): Call<CourseDetailResponse>

    @POST("clearedCourse/{account_id}/{course_id}")
    fun finishCourse(
        @Path("course_id") courseId: String?,
        @Path("account_id") userId: String?,
        @Header("Authorization") token: String
    ): Call<CourseDetailResponse>

    @GET("tryouts/account/{account_id}")
    fun getAllTryouts(
        @Path("account_id") userId: String?,
        @Header("Authorization") token: String
    ): Call<TryoutResponse>

    @GET("tryouts/{tryout_id}/account/{account_id}")
    fun getDetailTryout(
        @Path("tryout_id") tryoutId: String?,
        @Path("account_id") userId: String?,
        @Header("Authorization") token: String
    ): Call<TryoutDetailResponse>

    @GET("tryouts/newest")
    fun getNewestTryout(
        @Header("Authorization") token: String
    ): Call<TryoutResponse>

    @GET("freeTryouts/account/{account_id}")
    fun getFreeTryout(
        @Path("account_id") userId: String?,
        @Header("Authorization") token: String
    ): Call<TryoutResponse>

    @GET("payTryouts/account/{account_id}")
    fun getPaidTryout(
        @Path("account_id") userId: String?,
        @Header("Authorization") token: String
    ): Call<TryoutResponse>

    @GET("tryouts/{tryout_id}/start")
    fun startTryout(
        @Path("tryout_id") tryoutId: String?,
        @Header("Authorization") token: String
    ): Call<ExaminationResponse>

    @GET("tryouts/stats/{tryout_id}/{account_id}")
    fun getTryoutResult(
        @Path("tryout_id") tryoutId: String?,
        @Path("account_id") userId: String?,
        @Header("Authorization") token: String
    ): Call<TryoutResultResponse>

    @GET("tryouts/finished/{account_id}")
    fun getFinishedTryout(
        @Path("account_id") userId: String?,
        @Header("Authorization") token: String
    ): Call<FinishedTryoutResponse>

    @GET("tryoutBundles/account/{account_id}")
    fun getBundle(
        @Path("account_id") userId: String?,
        @Header("Authorization") token: String
    ): Call<TryoutBundleResponse>

    @GET("tryoutbundles/account/{account_id}/detail/{bundle_id}")
    fun getDetailBundle(
        @Path("account_id") userId: String?,
        @Path("bundle_id") bundleId: String?,
        @Header("Authorization") token: String
    ): Call<TryoutBundleDetailResponse>

    @POST("tryouts/cleared/{tryout_id}/{account_id}")
    fun clearTryout(
        @Path("tryout_id") tryoutId: String?,
        @Path("account_id") userId: String?,
        @Body request: TryoutRequest,
        @Header("Authorization") token: String
    ): Call<FinishTryoutResponse>

    @GET("leaderboard/{tryout_id}")
    fun getTryoutLeaderboard(
        @Path("tryout_id") tryoutId: String?,
        @Header("Authorization") token: String
    ): Call<LeaderboardResponse>

    @Multipart
    @POST("tryouts/transaction/{account_id}")
    fun sendTransaction(
        @Path("account_id") userId: String?,
        @Part bukti_transaksi: MultipartBody.Part,
        @Part("transaction_title") transactionTitle: RequestBody,
        @Part("transaction_price") transactionPrice: RequestBody,
        @Part("listTryout") listTryout: RequestBody,
        @Header("Authorization") token: String
    ): Call<PurchaseResponse>

    @GET("transaction/history/{account_id}")
    fun getTransactionHistory(
        @Path("account_id") userId: String?,
        @Header("Authorization") token: String
    ): Call<TransactionHistoryResponse>

    @GET("notifikasi/{account_id}")
    fun getNotification(
        @Path("account_id") userId: String?,
        @Header("Authorization") token: String
    ): Call<NotificationResponse>

    @PUT("updateNotif/{account_id}/{notifikasi_id}")
    fun updateNotification(
        @Path("account_id") userId: String?,
        @Path("notifikasi_id") notificationId: String?,
        @Header("Authorization") token: String
    ): Call<NotificationResponse>

    @GET("skd_analysis/{tryout_id}/{account_id}")
    fun getAnalysis(
        @Path("tryout_id") tryoutId: String?,
        @Path("account_id") userId: String?,
        @Header("Authorization") token: String
    ): Call<AnalysisResponse>

    @GET("latsol")
    fun getAllDrilling(
        @Header("Authorization") token: String
    ): Call<DrillingResponse>

    @GET("latsol/{latsol_id}")
    fun getDetailDrilling(
        @Path("latsol_id") latsolId: String?,
        @Header("Authorization") token: String
    ): Call<DrillingDetailResponse>

    @GET("latsol/{latsol_id}/account/{account_id}")
    fun getHistoryDrilling(
        @Path("latsol_id") latsolId: String?,
        @Path("account_id") userId: String?,
        @Header("Authorization") token: String
    ): Call<DrillingHistoryResponse>

    @GET("latsol/{latsol_id}/start")
    fun startDrilling(
        @Path("latsol_id") latsolId: String?,
        @Header("Authorization") token: String
    ): Call<DrillingStartResponse>

    @POST("latsol/{latsol_id}/account/{account_id}")
    fun finishDrilling(
        @Path("latsol_id") latsolId: String?,
        @Path("account_id") userId: String?,
        @Body request: DrillingRequest,
        @Header("Authorization") token: String
    ): Call<DrillingFinishResponse>

    @GET("historyLat/{historyLat_id}/account/{account_id}")
    fun getDetailScore(
        @Path("historyLat_id") historyLatId: String?,
        @Path("account_id") userId: String?,
        @Header("Authorization") token: String
    ): Call<DrillingScoreDetailResponse>

    @POST("redeem/{account_id}")
    fun redeemToken(
        @Path("account_id") userId: String?,
        @Body request: TokenRequest,
        @Header("Authorization") token: String
    ): Call<TokenResponse>

    @GET("popular")
    fun getPopularArticles(
        @Header("Authorization") token: String
    ): Call<List<PopularArticleResponse>>

    @GET("article")
    fun getNewestArticles(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Header("Authorization") token: String
    ): Call<NewestArticleResponse>
}
package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class LeaderboardResponse(

	@field:SerializedName("data")
	val data: List<LeaderboardItem>
)

data class LeaderboardItem(

	@field:SerializedName("tiu_score")
	val tiuScore: Int? = null,

	@field:SerializedName("tryout_passed")
	val tryoutPassed: String? = null,

	@field:SerializedName("tryout_id")
	val tryoutId: String? = null,

	@field:SerializedName("tryoutScore_id")
	val tryoutScoreId: String? = null,

	@field:SerializedName("twk_wrong")
	val twkWrong: Int? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("account_id")
	val accountId: String? = null,

	@field:SerializedName("twk_score")
	val twkScore: Int? = null,

	@field:SerializedName("tryout_score")
	val tryoutScore: Int? = null,

	@field:SerializedName("tkp_score")
	val tkpScore: Int? = null,

	@field:SerializedName("tiu_wrong")
	val tiuWrong: Int? = null,

	@field:SerializedName("account")
	val account: Account? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class Account(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("account_id")
	val accountId: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("userRecap")
	val userRecap: Any? = null,

	@field:SerializedName("avatar")
	val avatar: Any? = null,

	@field:SerializedName("accessToken")
	val accessToken: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

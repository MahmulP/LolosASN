package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class LeaderboardResponse(

	@field:SerializedName("data")
	val data: List<LeaderboardItem>
)

data class LeaderboardItem(

	@field:SerializedName("twk_wrong")
	val twkWrong: Int,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("account_id")
	val accountId: String,

	@field:SerializedName("twk_score")
	val twkScore: Int,

	@field:SerializedName("tryout_score")
	val tryoutScore: Int,

	@field:SerializedName("tiu_score")
	val tiuScore: Int,

	@field:SerializedName("tkp_score")
	val tkpScore: Int,

	@field:SerializedName("tiu_wrong")
	val tiuWrong: Int,

	@field:SerializedName("tryout_id")
	val tryoutId: String,

	@field:SerializedName("tryoutScore_id")
	val tryoutScoreId: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)

package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class TryoutResultResponse(

	@field:SerializedName("data")
	val data: DataResult
)

data class Tryout(

	@field:SerializedName("tryout_total")
	val tryoutTotal: Int? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("tryout_closed")
	val tryoutClosed: String? = null,

	@field:SerializedName("tryout_status")
	val tryoutStatus: String? = null,

	@field:SerializedName("tryout_title")
	val tryoutTitle: String? = null,

	@field:SerializedName("tryout_type")
	val tryoutType: String? = null,

	@field:SerializedName("tryout_duration")
	val tryoutDuration: Int? = null,

	@field:SerializedName("tryout_id")
	val tryoutId: String? = null,

	@field:SerializedName("tryout_price")
	val tryoutPrice: Int? = null,

	@field:SerializedName("tryout_file")
	val tryoutFile: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class DataResult(

	@field:SerializedName("tryout")
	val tryout: Tryout? = null,

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

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

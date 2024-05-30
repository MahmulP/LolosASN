package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class FinishedTryoutResponse(

	@field:SerializedName("data")
	val data: List<FinishedTryout>
)

data class FinishedTryout(

	@field:SerializedName("isCleared")
	val isCleared: String? = null,

	@field:SerializedName("tryout_status")
	val tryoutStatus: String? = null,

	@field:SerializedName("tryout_title")
	val tryoutTitle: String? = null,

	@field:SerializedName("tryout_type")
	val tryoutType: String? = null,

	@field:SerializedName("tryout_id")
	val tryoutId: String? = null,

	@field:SerializedName("tryout_price")
	val tryoutPrice: Int? = null,

	@field:SerializedName("accessed")
	val accessed: String? = null,

	@field:SerializedName("tryout_file")
	val tryoutFile: String? = null,

	@field:SerializedName("tryout_total")
	val tryoutTotal: Int? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("tryout_score")
	val tryoutScore: Int? = null,

	@field:SerializedName("tryout_passed")
	val tryoutPassed: String? = null,

	@field:SerializedName("tryout_closed")
	val tryoutClosed: String? = null,

	@field:SerializedName("tryout_duration")
	val tryoutDuration: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class TryoutResponse(
	val data: List<DataItem>
)

data class DataItem(
	@SerializedName("tryout_id") val tryoutId: String,
	@SerializedName("tryout_title") val tryoutTitle: String,
	@SerializedName("tryout_file") val tryoutFile: String,
	@SerializedName("tryout_duration") val tryoutDuration: Int,
	@SerializedName("tryout_total") val tryoutTotal: Int,
	@SerializedName("tryout_status") val tryoutStatus: String,
	@SerializedName("tryout_type") val tryoutType: String,
	@SerializedName("tryout_price") val tryoutPrice: Int,
	@SerializedName("tryout_closed") val tryoutClosed: String,
	@SerializedName("createdAt") val createdAt: String,
	@SerializedName("updatedAt") val updatedAt: String
)



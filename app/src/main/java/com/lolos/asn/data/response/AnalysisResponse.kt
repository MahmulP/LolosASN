package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class AnalysisResponse(

	@field:SerializedName("feedback")
	val feedback: Feedback? = null
)

data class Feedback(

	@field:SerializedName("tiu")
	val tiu: String? = null,

	@field:SerializedName("tkp")
	val tkp: String? = null,

	@field:SerializedName("twk")
	val twk: String? = null
)

package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class TryoutRequest(

	@field:SerializedName("twk_wrong")
	val twkWrong: String? = null,

	@field:SerializedName("twk_score")
	val twkScore: String? = null,

	@field:SerializedName("tiu_score")
	val tiuScore: String? = null,

	@field:SerializedName("tkp_score")
	val tkpScore: String? = null,

	@field:SerializedName("listCategory_score")
	val listCategoryScore: List<ListCategoryScoreItem?>? = null,

	@field:SerializedName("tiu_wrong")
	val tiuWrong: String? = null
)

data class ListCategoryScoreItem(

	@field:SerializedName("subCategory_score")
	val subCategoryScore: String,

	@field:SerializedName("subCategory_id")
	val subCategoryId: Int? = null
)

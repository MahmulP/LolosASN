package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class TryoutRequest(

	@field:SerializedName("twk_wrong")
	val twkWrong: Int? = null,

	@field:SerializedName("twk_score")
	val twkScore: Int? = null,

	@field:SerializedName("tryout_score")
	val tryoutScore: Int? = null,

	@field:SerializedName("tiu_score")
	val tiuScore: Int? = null,

	@field:SerializedName("tkp_score")
	val tkpScore: Int? = null,

	@field:SerializedName("listCategory_score")
	val listCategoryScore: List<ListCategoryScoreItem?>? = null,

	@field:SerializedName("tiu_wrong")
	val tiuWrong: Int? = null
)

data class ListCategoryScoreItem(

	@field:SerializedName("subCategory_score")
	val subCategoryScore: Int,

	@field:SerializedName("subCategory_id")
	val subCategoryId: Int,
)

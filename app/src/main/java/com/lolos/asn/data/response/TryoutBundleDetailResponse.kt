package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class TryoutBundleDetailResponse(

	@field:SerializedName("data")
	val data: DetailItem
)

data class DetailItem(

	@field:SerializedName("descList")
	val descList: List<String?>? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("tryoutBundle_id")
	val tryoutBundleId: String,

	@field:SerializedName("price")
	val price: String,

	@field:SerializedName("userBought")
	val userBought: List<String?>? = null,

	@field:SerializedName("base_price")
	val basePrice: Int,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("boolBought")
	val boolBought: Boolean? = null,

	@field:SerializedName("tryout_price")
	val tryoutPrice: Any? = null,

	@field:SerializedName("listTryout_id")
	val listTryoutId: List<String?>? = null,

	@field:SerializedName("tryoutBundle_name")
	val tryoutBundleName: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

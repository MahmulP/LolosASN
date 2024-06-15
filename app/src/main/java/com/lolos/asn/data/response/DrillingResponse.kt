package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class DrillingResponse(

	@field:SerializedName("data")
	val data: List<DrillingItem?>? = null
)

data class Category(

	@field:SerializedName("category_name")
	val categoryName: String? = null,

	@field:SerializedName("category_id")
	val categoryId: String? = null,

	@field:SerializedName("category_alias")
	val categoryAlias: String? = null
)

data class DrillingItem(

	@field:SerializedName("latsol_id")
	val latsolId: String? = null,

	@field:SerializedName("lat_file")
	val latFile: String? = null,

	@field:SerializedName("lat_thumb")
	val latThumb: String? = null,

	@field:SerializedName("lat_desc")
	val latDesc: String? = null,

	@field:SerializedName("waktu")
	val waktu: Int? = null,

	@field:SerializedName("category")
	val category: Category? = null,

	@field:SerializedName("jumlah_soal")
	val jumlahSoal: Int? = null
)

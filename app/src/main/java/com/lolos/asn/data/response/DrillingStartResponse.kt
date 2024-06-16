package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class DrillingStartResponse(

	@field:SerializedName("data")
	val data: DataStartItem? = null
)

data class CategoryStartItem(

	@field:SerializedName("category_name")
	val categoryName: String? = null,

	@field:SerializedName("category_id")
	val categoryId: String? = null,

	@field:SerializedName("category_alias")
	val categoryAlias: String? = null
)

data class DataStartItem(

	@field:SerializedName("lat_content")
	val latContent: List<List<LatContentItemItem>>,

	@field:SerializedName("lat_file")
	val latFile: String? = null,

	@field:SerializedName("lat_thumb")
	val latThumb: String? = null,

	@field:SerializedName("lat_desc")
	val latDesc: String? = null,

	@field:SerializedName("waktu")
	val waktu: Int? = null,

	@field:SerializedName("category")
	val category: CategoryStartItem? = null,

	@field:SerializedName("jumlah_soal")
	val jumlahSoal: Int? = null
)

data class LatContentItemItem(

	@field:SerializedName("pembahasan")
	val pembahasan: String? = null,

	@field:SerializedName("tryout_num")
	val tryoutNum: Int? = null,

	@field:SerializedName("question")
	val question: String? = null,

	@field:SerializedName("option_image")
	val optionImage: Int? = null,

	@field:SerializedName("handicap")
	val handicap: Int? = null,

	@field:SerializedName("question_image")
	val questionImage: Any? = null,

	@field:SerializedName("subCategory_id")
	val subCategoryId: Int? = null,

	@field:SerializedName("jawaban")
	val jawaban: Int? = null,

	@field:SerializedName("category")
	val category: Int? = null,

	@field:SerializedName("option")
	val option: List<String?>? = null
)

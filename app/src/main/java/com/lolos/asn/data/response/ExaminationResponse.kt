package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class ExaminationResponse(

	@field:SerializedName("data")
	val data: TryoutData
)

data class TryoutContentItem(

	@field:SerializedName("pembahasan")
	val pembahasan: String? = null,

	@field:SerializedName("tryout_num")
	val tryoutNum: String? = null,

	@field:SerializedName("question")
	val question: String? = null,

	@field:SerializedName("option_image")
	val optionImage: Boolean? = null,

	@field:SerializedName("question_image")
	val questionImage: String? = null,

	@field:SerializedName("subCategory_id")
	val subCategoryId: String? = null,

//	@field:SerializedName("jawaban")
//	val jawaban: List<Int?>? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("option")
	val option: List<String?>? = null

)

data class TryoutData(

	@field:SerializedName("tryout_total")
	val tryoutTotal: Int? = null,

	@field:SerializedName("tryout_title")
	val tryoutTitle: String? = null,

	@field:SerializedName("tryout_duration")
	val tryoutDuration: Int,

	@field:SerializedName("tryout_id")
	val tryoutId: String? = null,

	@field:SerializedName("tryout_content")
	val tryoutContent: List<TryoutContentItem?>? = null
)

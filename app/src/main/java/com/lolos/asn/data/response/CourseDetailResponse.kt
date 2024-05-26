package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class CourseDetailResponse(
	val data: DetailResponse? = null
)

data class DetailResponse(
	@SerializedName("isCleared") val isCleared: String? = null,
	@SerializedName("course_id") val courseId: String? = null,
	@SerializedName("createdAt") val createdAt: String? = null,
	@SerializedName("course_queue") val courseQueue: Int? = null,
	@SerializedName("category_id") val categoryId: String? = null,
	@SerializedName("course_name") val courseName: String? = null,
	@SerializedName("course_file") val courseFile: String? = null,
	@SerializedName("course_image") val courseImage: String? = null,
	@SerializedName("course_description") val courseDescription: String? = null,
	@SerializedName("content") val content: List<String?>? = null,
	@SerializedName("updatedAt") val updatedAt: String? = null
)


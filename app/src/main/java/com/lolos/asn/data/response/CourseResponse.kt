package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class CourseResponse(
	val data: List<Course>
)

data class Course(
	@SerializedName("course_id") val courseId: String,
	@SerializedName("category_id") val categoryId: String,
	@SerializedName("course_image") val courseImage: String,
	@SerializedName("course_queue") val courseQueue: Int,
	@SerializedName("course_name") val courseName: String,
	@SerializedName("course_description") val courseDescription: String?,
	@SerializedName("course_file") val courseFile: String?,
	@SerializedName("createdAt") val createdAt: String,
	@SerializedName("updatedAt") val updatedAt: String,
	@SerializedName("isCleared") val isCleared: String
)


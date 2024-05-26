package com.lolos.asn.data.response

data class UserResponse(
	val data: Data
)

data class Data(
	val createdAt: String,
	val password: String,
	val accountId: String,
	val role: String,
	val name: String,
	val userRecap: Any,
	val avatar: String,
	val accessToken: Any,
	val email: String,
	val username: String,
	val updatedAt: String
)

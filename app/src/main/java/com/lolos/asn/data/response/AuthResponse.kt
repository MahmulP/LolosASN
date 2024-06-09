package com.lolos.asn.data.response

data class LoginResponse(
	val userId: String,
	val name: String,
	val data: String,
	val message: String? = null
)

data class LoginRequest(
	val email: String,
	val password: String
)

data class RegisterResponse(
	val message: String? = null
)

data class RegisterRequest(
	val name: String,
	val email: String,
	val password: String,
	val phone: String
)

data class UserDataResponse(
	val name: String?,
	val email: String?,
	val role: String?,
	val phone: String?,
	val avatar: String?,
)


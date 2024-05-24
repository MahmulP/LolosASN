package com.lolos.asn.data.response

data class LoginResponse(
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
	val password: String
)


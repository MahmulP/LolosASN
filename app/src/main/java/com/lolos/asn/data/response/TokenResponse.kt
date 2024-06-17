package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class TokenResponse(

	@field:SerializedName("message")
	val message: String? = null
)

data class TokenRequest(

	@field:SerializedName("tryoutToken_code")
	val tryoutTokenCode: String? = null

)

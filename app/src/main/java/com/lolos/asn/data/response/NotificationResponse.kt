package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class NotificationResponse(

	@field:SerializedName("data")
	val data: List<NotificationItem>
)

data class NotificationItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("account_id")
	val accountId: String? = null,

	@field:SerializedName("notifikasi_msg")
	val notifikasiMsg: String? = null,

	@field:SerializedName("notifikasi_id")
	val notifikasiId: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

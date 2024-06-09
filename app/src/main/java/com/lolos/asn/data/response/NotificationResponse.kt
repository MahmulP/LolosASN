package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class NotificationResponse(

	@field:SerializedName("data")
	val data: List<NotificationItem>
)

data class NotificationItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("account_id")
	val accountId: String? = null,

	@field:SerializedName("notifikasi_msg")
	val notifikasiMsg: String? = null,

	@field:SerializedName("notifikasi_id")
	val notifikasiId: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null,

	@field:SerializedName("isClicked")
	val isClicked: Boolean? = null,

	@field:SerializedName("notifikasi_title")
	val notificationTitle: String? = null

)

sealed class NotificationDisplayItem {
	data class Header(val title: String) : NotificationDisplayItem()
	data class DisplayItem(val notification: NotificationItem) : NotificationDisplayItem()
}

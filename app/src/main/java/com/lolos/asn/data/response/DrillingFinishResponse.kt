package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class DrillingFinishResponse(

	@field:SerializedName("data")
	val data: DataFinishItem? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataFinishItem(

	@field:SerializedName("total_kosong")
	val totalKosong: Int? = null,

	@field:SerializedName("latsol_id")
	val latsolId: String? = null,

	@field:SerializedName("total_salah")
	val totalSalah: Int? = null,

	@field:SerializedName("total_mudah")
	val totalMudah: Int? = null,

	@field:SerializedName("total_sedang")
	val totalSedang: Int? = null,

	@field:SerializedName("total_susah")
	val totalSusah: Int? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("historyLat_id")
	val historyLatId: String? = null,

	@field:SerializedName("account_id")
	val accountId: String? = null,

	@field:SerializedName("total_score")
	val totalScore: Int? = null,

	@field:SerializedName("total_pengerjaan")
	val totalPengerjaan: Int? = null,

	@field:SerializedName("total_benar")
	val totalBenar: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

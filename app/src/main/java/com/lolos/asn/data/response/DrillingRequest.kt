package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class DrillingRequest(

	@field:SerializedName("total_kosong")
	val totalKosong: Int? = null,

	@field:SerializedName("total_salah")
	val totalSalah: Int? = null,

	@field:SerializedName("total_mudah")
	val totalMudah: Int? = null,

	@field:SerializedName("total_sedang")
	val totalSedang: Int? = null,

	@field:SerializedName("total_susah")
	val totalSusah: Int? = null,

	@field:SerializedName("total_pengerjaan")
	val totalPengerjaan: Int? = null,

	@field:SerializedName("total_benar")
	val totalBenar: Int? = null
)

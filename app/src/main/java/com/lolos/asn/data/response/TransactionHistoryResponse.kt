package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class TransactionHistoryResponse(

	@field:SerializedName("data")
	val data: List<TransactionItem>
)

data class TransactionItem(

	@field:SerializedName("jumlah_to")
	val jumlahTo: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: Any? = null,

	@field:SerializedName("transaction_status")
	val transactionStatus: String? = null,

	@field:SerializedName("account_id")
	val accountId: String? = null,

	@field:SerializedName("transaction_title")
	val transactionTitle: String? = null,

	@field:SerializedName("transaction_price")
	val transactionPrice: Int? = null,

	@field:SerializedName("listTryout")
	val listTryout: String? = null,

	@field:SerializedName("transactionRecord_id")
	val transactionRecordId: String? = null,

	@field:SerializedName("bukti_transaski")
	val buktiTransaski: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: Any? = null
)

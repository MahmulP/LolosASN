package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class NewestArticleResponse(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("pages")
	val pages: Int? = null,

	@field:SerializedName("size")
	val size: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("items")
	val items: List<ItemsItem?>? = null
)

data class ItemsItem(

	@field:SerializedName("themes")
	val themes: String? = null,

	@field:SerializedName("link_berita")
	val linkBerita: String? = null,

	@field:SerializedName("link_gambar")
	val linkGambar: String? = null,

	@field:SerializedName("recomendation")
	val recomendation: String? = null,

	@field:SerializedName("judul_berita")
	val judulBerita: String? = null
)

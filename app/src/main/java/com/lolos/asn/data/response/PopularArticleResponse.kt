package com.lolos.asn.data.response

import com.google.gson.annotations.SerializedName

data class PopularArticleResponse(

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

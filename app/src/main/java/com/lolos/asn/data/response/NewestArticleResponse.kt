package com.lolos.asn.data.response

import androidx.room.Entity
import androidx.room.PrimaryKey
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
	val items: List<ArticleResponseItem?>? = null
)

@Entity(tableName = "article")
data class ArticleResponseItem(

	@field:SerializedName("themes")
	val themes: String? = null,

	@field:SerializedName("link_berita")
	val linkBerita: String? = null,

	@field:SerializedName("link_gambar")
	val linkGambar: String? = null,

	@field:SerializedName("recomendation")
	val recomendation: String? = null,

	@PrimaryKey
	@field:SerializedName("judul_berita")
	val judulBerita: String
)

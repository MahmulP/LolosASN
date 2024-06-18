package com.lolos.asn.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lolos.asn.R
import com.lolos.asn.data.response.PopularArticleResponse
import com.lolos.asn.databinding.ListPopularArticleBinding

class ArticlePopularAdapter(private val context: Context): ListAdapter<PopularArticleResponse, ArticlePopularAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListPopularArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article, context)
    }
    class MyViewHolder(val binding: ListPopularArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(article: PopularArticleResponse, context: Context) {
            val thumbnail = article.linkGambar

            binding.tvArticleTitle.text = article.judulBerita

            Glide.with(binding.ivArticle.context)
                .load(thumbnail)
                .error(R.drawable.no_image)
                .into(binding.ivArticle)
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PopularArticleResponse>() {
            override fun areItemsTheSame(oldItem: PopularArticleResponse, newItem: PopularArticleResponse): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: PopularArticleResponse, newItem: PopularArticleResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}
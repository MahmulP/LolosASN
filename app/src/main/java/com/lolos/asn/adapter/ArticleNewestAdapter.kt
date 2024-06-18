package com.lolos.asn.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lolos.asn.R
import com.lolos.asn.data.response.ArticleResponseItem
import com.lolos.asn.databinding.ListNewestArticleBinding
import com.lolos.asn.ui.activity.ArticleDetailActivity

class ArticleNewestAdapter(private val context: Context) :
    PagingDataAdapter<ArticleResponseItem, ArticleNewestAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListNewestArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data, context)

            holder.itemView.setOnClickListener {
                val intent = Intent(context, ArticleDetailActivity::class.java)
                intent.putExtra("alamat_url", data.linkBerita)
                context.startActivity(intent)
            }
        }
    }

    class MyViewHolder(private val binding: ListNewestArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ArticleResponseItem, context: Context) {
            val thumbnail = data.linkGambar

            binding.tvTitleContent.text = data.judulBerita

            Glide.with(binding.ivContent.context)
                .load(thumbnail)
                .error(R.drawable.no_image)
                .into(binding.ivContent)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticleResponseItem>() {
            override fun areItemsTheSame(oldItem: ArticleResponseItem, newItem: ArticleResponseItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticleResponseItem, newItem: ArticleResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
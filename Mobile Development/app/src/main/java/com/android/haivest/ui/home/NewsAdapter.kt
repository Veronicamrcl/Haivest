package com.android.haivest.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.haivest.data.network.response.Article
import com.android.haivest.databinding.ItemInsightBinding
import com.bumptech.glide.Glide

class NewsAdapter(private val onClick: (Article) -> Unit) : ListAdapter<Article, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemInsightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    class NewsViewHolder(private val binding: ItemInsightBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article, onClick: (Article) -> Unit) {
            binding.articlesTitle.text = article.title
            Glide.with(binding.articlesImage.context)
                .load(article.urlToImage)
                .into(binding.articlesImage)
            binding.root.setOnClickListener { onClick(article) }
        }
    }

    class NewsDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}

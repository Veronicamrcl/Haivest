package com.android.haivest.ui.detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.haivest.R
import com.android.haivest.data.network.response.Article
import com.android.haivest.databinding.ActivityDetailNewsBinding
import com.bumptech.glide.Glide

class DetailNewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION") val article = intent.getParcelableExtra<Article>("article")
        article?.let {
            Glide.with(this)
                .load(it.urlToImage)
                .into(binding.imageArticle)
            binding.apply {
                titleArticle.text = it.title
                authorArticle.text = it.author
                publishedArticle.text = it.publishedAt
                descriptionArticle.text = it.description
                contentArticle.text = it.content
                sourceArticle.text = it.url
            }

        }

        binding.btnBack.setOnClickListener {
            finish()
        }

    }
}
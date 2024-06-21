package com.android.haivest.data.network.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
) : Parcelable

@Parcelize
data class Article(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
) : Parcelable

@Parcelize
data class Source(
    val id: String?,
    val name: String
) : Parcelable
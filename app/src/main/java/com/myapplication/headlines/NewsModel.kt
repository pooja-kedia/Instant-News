package com.myapplication.headlines

import android.media.Image

data class NewsModel(
    val newspaperLogo: String,
    val newsImage: String,
    val newsHeadline: String,
    val time: String
)
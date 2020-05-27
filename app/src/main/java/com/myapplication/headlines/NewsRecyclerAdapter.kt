package com.myapplication.headlines

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myapplication.R

class NewsRecyclerAdapter(
    private val listNews: ArrayList<NewsModel>,
    private val context: Context
) : RecyclerView.Adapter<NewsRecyclerAdapter.NewsHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return listNews.size
    }

    inner class NewsHolder(view: View) : RecyclerView.ViewHolder(view) {
        val newspaperLogo: TextView = view.findViewById(R.id.newspaper_logo)
        val newsDetail: ImageView = view.findViewById(R.id.img_news_detail)
        val newsHeadline: TextView = view.findViewById(R.id.news_headline)
        val timeText: TextView = view.findViewById(R.id.time_text)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val item = listNews[position]
        holder.newsHeadline.text = item.newsHeadline
        holder.timeText.text = item.time
        Glide.with(context).load(item.newsImage).into(holder.newsDetail)
        holder.newspaperLogo.text = item.newspaperLogo
    }

}

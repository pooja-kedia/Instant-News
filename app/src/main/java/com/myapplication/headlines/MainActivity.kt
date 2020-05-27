package com.myapplication.headlines

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request.Method.GET
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.myapplication.MySingleton
import com.myapplication.R
import com.myapplication.newspaper.NewsPaperListActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var url = "https://newsapi.org/v2/top-headlines?country=in&pageSize=100&apiKey=83b8d04f4a444519ae239af5327c9bca"
        //dusri Activity se data lene ke liye (intent.extras)
        intent.extras?.let{
            val clickedId =   it.getString("clickedId")
            url = "https://newsapi.org/v2/top-headlines?sources=$clickedId&pageSize=100&apiKey=83b8d04f4a444519ae239af5327c9bca"

            fab.visibility = View.GONE
        }

        fetchNews(url)
    }

    private fun fetchNews(url: String) {
        // Get a RequestQueue
        val listNews: ArrayList<NewsModel> = ArrayList()
        val stringRequest =
            StringRequest(GET, url, Response.Listener {

                //Log.d("VolleyResponse", it)
                val jsonObj = JSONObject(it)
                val articlesArray = jsonObj.getJSONArray("articles")
                for (i in 0 until articlesArray.length()) {
                    val article = articlesArray[i] as JSONObject
                    val title = article.getString("title")
                    val urlToImage = article.getString("urlToImage")
                    val publishedAt = article.getString("publishedAt").replace("T"," ").replace("Z"," ")
                    val sources = article.getJSONObject("source")
                    val name = sources.getString("name")
                    val newsModel = NewsModel(
                        name,
                        urlToImage,
                        title,
                        publishedAt
                    )
                    listNews.add(newsModel)
                }
                //Testzxhkhk
                val newsRecyclerAdapter = NewsRecyclerAdapter(listNews, this)
                recycler_news.layoutManager = LinearLayoutManager(this)
                recycler_news.adapter = newsRecyclerAdapter

            }, Response.ErrorListener {

            })

        // Add a request (in this example, called stringRequest) to your RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(stringRequest)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val url = "https://newsapi.org/v2/everything?q=$query&pageSize=100&apiKey=83b8d04f4a444519ae239af5327c9bca"
                fetchNews(url)

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    fun onNewsListClick(view: View) {
        val intent = Intent(this,
            NewsPaperListActivity::class.java)
        startActivity(intent)

    }
}

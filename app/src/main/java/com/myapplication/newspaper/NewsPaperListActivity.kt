package com.myapplication.newspaper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request.Method.GET
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.myapplication.MySingleton
import com.myapplication.R
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class NewsPaperListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_paper_list)

//        supportActionBar!!.title = "News Paper List"

        val url = "https://newsapi.org/v2/sources?&size=100&apiKey=83b8d04f4a444519ae239af5327c9bca"
        fetchNews(url)
    }

    private fun fetchNews(url: String) {
        val listNewspaper : ArrayList<NewspaperModel> = ArrayList()
        val stringRequest = StringRequest(GET,url,Response.Listener {

            val jsonObject = JSONObject(it)
            val sourcesArray= jsonObject.getJSONArray("sources")
            for(i in 0 until sourcesArray.length() ){
                val sources = sourcesArray[i] as JSONObject
                val name = sources.getString("name")
                val id = sources.getString("id")
                val newspaperModel = NewspaperModel(
                    name,
                    id
                )
                listNewspaper.add(newspaperModel)
            }
            val newspaperRecyclerAdapter = NewspaperRecyclerAdapter(listNewspaper,this)
            recycler_news.layoutManager = LinearLayoutManager(this)
            recycler_news.adapter = newspaperRecyclerAdapter


        },Response.ErrorListener {})

        MySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }
}

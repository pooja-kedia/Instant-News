package com.myapplication.headlines

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request.Method.GET
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.android.material.navigation.NavigationView
import com.myapplication.MySingleton
import com.myapplication.R
import com.myapplication.newspaper.NewsPaperListActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //hamburger icon(Menu) ko Nevigation drawer se jodne ke liye setUpToolbar banaya.
        setupToolbar()

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)


        var url =
            "https://newsapi.org/v2/top-headlines?country=in&pageSize=100&apiKey=83b8d04f4a444519ae239af5327c9bca"
        //dusri Activity se data lene ke liye (intent.extras)
        intent.extras?.let {
            val clickedId = it.getString("clickedId")
            clickedId?.let { source ->
                url =
                    "https://newsapi.org/v2/top-headlines?sources=$source&pageSize=100&apiKey=83b8d04f4a444519ae239af5327c9bca"
                fab.visibility = View.GONE
            }

        }

        fetchNews(url)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_business -> {
                fetchNews("https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=83b8d04f4a444519ae239af5327c9bca")
                //Toast.makeText(this, "Business clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_technology -> {
                fetchNews("https://newsapi.org/v2/top-headlines?country=in&category=technology&apiKey=83b8d04f4a444519ae239af5327c9bca")
            }
            R.id.nav_entertainment -> {
                fetchNews("https://newsapi.org/v2/top-headlines?country=in&category=entertainment&apiKey=83b8d04f4a444519ae239af5327c9bca")
            }
            R.id.nav_sports -> {
                fetchNews("https://newsapi.org/v2/top-headlines?country=in&category=sports&apiKey=83b8d04f4a444519ae239af5327c9bca")
            }
            R.id.nav_science -> {
                fetchNews("https://newsapi.org/v2/top-headlines?country=in&category=science&apiKey=83b8d04f4a444519ae239af5327c9bca")
            }
            R.id.nav_health -> {
                fetchNews("https://newsapi.org/v2/top-headlines?country=in&category=health&apiKey=83b8d04f4a444519ae239af5327c9bca")
            }
            R.id.nav_share -> {
                Toast.makeText(this, "Share clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_sign_out -> {
                Toast.makeText(this, "Sign out clicked", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun fetchNews(url: String) {
        // Get a RequestQueue

        progress_circular.visibility = View.VISIBLE

        Log.d("VolleyResponse", "fetchNews url $url")
        val listNews: ArrayList<NewsModel> = ArrayList()
        val stringRequest =
            StringRequest(GET, url, Response.Listener {
                val jsonObj = JSONObject(it)

                progress_circular.visibility = View.GONE

                Log.d("VolleyResponse", it)
                val articlesArray = jsonObj.getJSONArray("articles")
                for (i in 0 until articlesArray.length()) {
                    val article = articlesArray[i] as JSONObject
                    val title = article.getString("title")
                    val urlToImage = article.getString("urlToImage")
                    val publishedAt =
                        article.getString("publishedAt").replace("T", " ").replace("Z", " ")
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
                Log.d("VolleyResponse", "it $it")
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
                val url =
                    "https://newsapi.org/v2/everything?q=$query&pageSize=100&apiKey=83b8d04f4a444519ae239af5327c9bca"
                fetchNews(url)

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawer_layout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun onNewsListClick(view: View) {
        val intent = Intent(
            this,
            NewsPaperListActivity::class.java
        )
        startActivity(intent)

    }
}

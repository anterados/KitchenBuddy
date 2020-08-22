package com.example.kitchenbuddy

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_home.*
import okhttp3.*
import okio.GzipSource
import java.io.IOException

class HomeActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //recyclerView_home.setBackgroundColor(Color.BLUE)
        recyclerView_home.layoutManager=LinearLayoutManager(this)
        //recyclerView_home.adapter=HomeAdapter()

        fetchJson()
    }
    fun fetchJson(){
        println("Attempting to fetch JSON")
        val url = "http://www.recipepuppy.com/api/"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object:Callback{
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()
                val homeList = gson.fromJson(body, HomeList::class.java)
                runOnUiThread{
                    recyclerView_home.adapter=HomeAdapter(homeList)
                }

            }
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }
}

class HomeList(val results: List<Recipe>)
class Recipe(val title:String, val ingredients:String, val thumbnail:String)

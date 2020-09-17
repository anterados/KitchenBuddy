package com.example.kitchenbuddy

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_ingredient.*
import kotlinx.android.synthetic.main.activity_recipe.*
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity() {


    private var emptyData: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //recyclerView_home.setBackgroundColor(Color.BLUE)
        recyclerView_home.layoutManager=LinearLayoutManager(this)
        //recyclerView_home.adapter=HomeAdapter()
        //val intent = intent
        emptyData = findViewById(R.id.empty_view) as TextView
        //handleIntent(getIntent())
        fetchJson()
        recyclerView_home.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        if (menu != null) {
            menu.removeItem(R.id.action_search)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.title){
            "My favorites" -> {
                val intent = Intent(this, FavoritesActivity::class.java)
                startActivity(intent)

            }
            "Log out" -> {
                FirebaseAuth.getInstance().signOut()
                finish()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "You signed out successfully!", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun fetchJson(){
        println("Attempting to fetch JSON")
        var getList = intent.getSerializableExtra("key") as ArrayList<String>
        //val url = "http://www.recipepuppy.com/api/"
        //val url = "https://www.themealdb.com/api/json/v2/9973533/filter.php?i="
        var stringIngredient=getList.joinToString(separator = ",").replace(" ", "_").toLowerCase()

        if(stringIngredient==""){
            stringIngredient="0"
        }
        val url = "https://www.themealdb.com/api/json/v2/9973533/filter.php?i="+"${stringIngredient.toString()}"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object:Callback{
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)
                if (body != null) {
                    if(body.contains("Undefined variable")){
                        Log.d("HomeActivity","BELAJ!")
                        Toast.makeText(this@HomeActivity, "Too many ingredients selected!", Toast.LENGTH_SHORT).show()
                }
                    else{
                        val gson = GsonBuilder().create()
                        val homeList = gson.fromJson(body, HomeList::class.java)

                        if(homeList.meals.isNullOrEmpty()) {
                            //Toast.makeText(this@HomeActivity, "No ingredients selected!", Toast.LENGTH_SHORT).show()
                            //recyclerView_home.visibility = View.GONE
                            //empty_view.visibility=View.VISIBLE
                            //val intent = Intent(this@HomeActivity, IngredientActivity::class.java)

                        }
                        else {
                            runOnUiThread {
                                emptyData!!.setVisibility(View.GONE)

                                recyclerView_home.adapter = HomeAdapter(homeList)
                            }
                        }
                    }
                }

            }
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }

    fun searching(search : SearchView){
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("HomeActivity","Llego al querysubmit")
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.i("HomeActivity","Llego al querytextchange")
                return true
            }
        })
    }
    fun doMySearch(query: String){
        Log.d("HomeActivity", "nesta!")
        Log.d("HomeActivity", query)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        getIntent()
        handleIntent(intent!!)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            val result=query
            Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
            Log.d("HomeActivity", "nesta!")
            Log.d("HomeActivity", "$result")
        }
    }
}




class HomeList(val meals: List<Recipe>)
class Recipe(val strMeal:String, val strMealThumb:String, val idMeal:String)




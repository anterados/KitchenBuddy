package com.example.kitchenbuddy

//import android.R
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_ingredient.*
import okhttp3.*
import java.io.IOException
import java.util.*


class IngredientActivity:AppCompatActivity() {


    val sastojak1=Ingredient(idIngredient = "1", strIngredient = "sastojak_1", strDescription = "opis1")
    val sastojak2=Ingredient(idIngredient = "2", strIngredient = "sastojak_2", strDescription = "opis2")
    val sastojak3=Ingredient(idIngredient = "3", strIngredient = "sastojak_3", strDescription = "opis3")
    val prva = mutableListOf(sastojak1,sastojak2,sastojak3)
    var lista = IngredientList(prva)
    val broj = lista.meals.count()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredient)
        Log.v("IngredientActivity", "$broj + AAAAAAAA")
        //recyclerView_home.setBackgroundColor(Color.BLUE)
        recyclerView_ingredient.layoutManager= LinearLayoutManager(this)
        //recyclerView_home.adapter=HomeAdapter()
        //val intent = intent
/*
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                doMySearch(query)
            }

 */
        //handleIntent(getIntent())
        fetchJson()
        Log.v("IngredientActivity", "${lista.meals.count()} + AAAAAAAA")
    }

    fun fetchJson(){
        println("Attempting to fetch JSON")
        //val url = "http://www.recipepuppy.com/api/"
        val url = "https://www.themealdb.com/api/json/v2/9973533/list.php?i=list"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()
                val ingredientList = gson.fromJson(body, IngredientList::class.java)

                runOnUiThread{
                    recyclerView_ingredient.adapter=IngredientAdapter(ingredientList)
                }

            }
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu!!.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))


        val menuItem=menu!!.findItem(R.id.action_search)
        if (menuItem != null){
            val searchView = menuItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.v("IngredientActivity", "$query")
                   return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText!!.isNotEmpty()){
                        val search = newText.toLowerCase(Locale.getDefault())
                        Log.v("IngredientActivity", "DAAAAA")
                        onQueryTextSubmit(search)

                    }
                    else{
                        Log.v("IngredientActivity", "NEEEE")
                    }
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }


    fun searching(search : SearchView){
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("IngredientActivity","Llego al querysubmit")
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.i("IngredientActivity","Llego al querytextchange")
                return true
            }
        })
    }
    fun doMySearch(query: String){
        Log.d("IngredientActivity", "nesta!")
        Log.d("IngredientActivity", query)
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




class IngredientList(val meals: MutableList<Ingredient>)
class Ingredient(val idIngredient:String, val strIngredient:String, val strDescription:String )



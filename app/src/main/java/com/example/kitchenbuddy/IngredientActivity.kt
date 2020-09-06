package com.example.kitchenbuddy

//import android.R
import android.app.SearchManager
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.graphics.ColorSpace
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_ingredient.*
import kotlinx.android.synthetic.main.recipe_row.*
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class IngredientActivity:AppCompatActivity(), CallbackInterface {

/*
    val sastojak1=Ingredient(idIngredient = "1", strIngredient = "sastojak_1", strDescription = "opis1")
    val sastojak2=Ingredient(idIngredient = "2", strIngredient = "sastojak_2", strDescription = "opis2")
    val sastojak3=Ingredient(idIngredient = "3", strIngredient = "sastojak_3", strDescription = "opis3")
    val prva = arrayListOf(sastojak1,sastojak2,sastojak3)
    var lista = IngredientList(prva)
    val broj = lista.meals.count()

    val lista1=lista
*/
    var ingredientListforRecipe=ArrayList<String>()
    val arrayList = ArrayList<Ingredient>()
    val displayList = ArrayList<Ingredient>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredient)

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
        //



        fetchJson()


        /*
        runOnUiThread{
            recyclerView_ingredient.adapter=IngredientAdapter(IngredientList(displayList))
        }

*/
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
                arrayList.addAll(ingredientList.meals)
                displayList.addAll(arrayList)
                Log.v("IngredientActivity", "INGR")

                runOnUiThread{
                    recyclerView_ingredient.adapter=IngredientAdapter(IngredientList(displayList), this@IngredientActivity)
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
                        displayList.clear()
                        val search = newText.toLowerCase(Locale.getDefault())
                        Log.v("IngredientActivity", "DAAAAA")
                        arrayList.forEach {
                            if (it.strIngredient.toLowerCase(Locale.getDefault()).contains(search)){
                                displayList.add(it)
                            }
                        }
                        //onQueryTextSubmit(search)
                        recyclerView_ingredient.adapter!!.notifyDataSetChanged()
                    }
                    else{
                        displayList.clear()
                        displayList.addAll(arrayList)
                        recyclerView_ingredient.adapter!!.notifyDataSetChanged()
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

    override fun passDataCallback(message: String) {
        Log.d("IngredientActivity", "$message")
        if(!ingredientListforRecipe.contains(message)){
            ingredientListforRecipe.add(message)
        }
        else
        {
            ingredientListforRecipe.remove(message)
        }
        Log.d("IngredientActivity", "${ingredientListforRecipe.toString()}")
        //ingredient_textview_ingredient.text="Your ingredients:"+"${ingredientListforRecipe.joinToString(separator = ", ")}"
        ingredient_textview_ingredient.text="Your ingredients:"+"${(ingredientListforRecipe.joinToString(separator = ",")).replace(" ", "_").toLowerCase()}"
        Log.d("IngredientActivity", "${ingredientListforRecipe.count().toString()}")
        //val intent=Intent(this, HomeActivity::class.java )
        //intent.putExtra("key",ingredientListforRecipe)
        //startActivity(intent)
        findRecipeClick()
    }

    fun findRecipeClick(){
        if(ingredientListforRecipe != null && !ingredientListforRecipe.isEmpty() && ingredientListforRecipe.count()!=0) {
            find_recipe_button_ingredient.setOnClickListener {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("key",ingredientListforRecipe)
                startActivity(intent)
            }
        }
        else
            //find_recipe_button_ingredient.isClickable=false
            find_recipe_button_ingredient.setOnClickListener {
                Toast.makeText(this, "No ingredients selected!", Toast.LENGTH_SHORT).show()
            }

    }

    class IngredientList(var meals: ArrayList<Ingredient>)
    class Ingredient(val idIngredient:String, val strIngredient:String, val strDescription:String )
}








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
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_ingredient.*
import kotlinx.android.synthetic.main.recipe_row.*
import kotlinx.android.synthetic.main.recipe_row.view.*
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



        fetchUsername()
        fetchJson()

        removeAll()

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
        ingredient_textview_ingredient.text="Your ingredients: "+"${ingredientListforRecipe.joinToString(separator = ", ")}"
        Log.d("IngredientActivity", "${ingredientListforRecipe.count().toString()}")

        findRecipeClick()

    }

    fun findRecipeClick(){
        Log.d("IngredientActivity", "COUNT:${ingredientListforRecipe.count().toString()}")
        Log.d("IngredientActivity", "SIZE:${ingredientListforRecipe.size.toString()}")
        if(ingredientListforRecipe.count() in 1..4) {
            find_recipe_button_ingredient.setOnClickListener {
                Log.d("IngredientActivity", "OCISCENA:${ingredientListforRecipe.toString()}")
                Log.d("IngredientActivity", "BROJ:${ingredientListforRecipe.size.toString()}")
                //removeAll()
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("key",ingredientListforRecipe)
                startActivity(intent)
            }
        }
        else if(ingredientListforRecipe.count()==0)
            //find_recipe_button_ingredient.isClickable=false
            find_recipe_button_ingredient.setOnClickListener {
                Toast.makeText(this@IngredientActivity, "No ingredients selected!", Toast.LENGTH_SHORT).show()
            }
        else if(ingredientListforRecipe.count()>4)
            find_recipe_button_ingredient.setOnClickListener {
                Toast.makeText(this@IngredientActivity, "Too many ingredients selected!", Toast.LENGTH_SHORT).show()
            }

    }

    fun removeAll(){
        if(ingredientListforRecipe != null || !ingredientListforRecipe.isEmpty() || ingredientListforRecipe.count()!=0) {
            button.setOnClickListener {
                ingredientListforRecipe.clear()
                Log.d("IngredientActivity", "OCISCENA:${ingredientListforRecipe.toString()}")
                ingredient_textview_ingredient.text="Your ingredients:"
            }
        }
        else if(ingredientListforRecipe.count()==0)
        //find_recipe_button_ingredient.isClickable=false
            button.setOnClickListener {
                Toast.makeText(this@IngredientActivity, "List already empty", Toast.LENGTH_SHORT).show()
            }

    }
    fun fetchUsername(){
        val uid = FirebaseAuth.getInstance().uid ?:""
        var returnValue:Float=0F
        val ref = FirebaseDatabase.getInstance().getReference().child("users").child("$uid").child("username")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var post = dataSnapshot.getValue()
                if (post==null){
                    post="to KitchenBuddy"
                }
                Toast.makeText(this@IngredientActivity, "Welcome ${post.toString()}!", Toast.LENGTH_SHORT).show()

            }
            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException() // don't ignore errors
                Log.d("RecipeActivity", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }


    class IngredientList(var meals: ArrayList<Ingredient>)
    class Ingredient(val idIngredient:String, val strIngredient:String, val strDescription:String )
}








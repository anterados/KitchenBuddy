package com.example.kitchenbuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.recipe_row.*
import okhttp3.*
import java.io.IOException

class RecipeActivity:AppCompatActivity() {

    //val recipeId = intent.getSerializableExtra("id") as String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recipeId = intent.getSerializableExtra("id") as String
        Log.d("RecipeActivity", "ID is: $recipeId")
        setContentView(R.layout.activity_recipe)

        fetchJson(recipeId)
        //recipeTitle_textView.text=recipeId.toString()

        findStore_button.setOnClickListener {
            val intent = Intent(this, FindStoresActivity::class.java)
            startActivity(intent)
        }

    }

    fun fetchJson(recipeId:String){
        println("Attempting to fetch JSON")
        val url = "https://www.themealdb.com/api/json/v2/9973533/lookup.php?i="+"${recipeId.toString()}"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()
                val recipefull = gson.fromJson(body, RecipeFull::class.java)
                Log.d("RecipeActivity", "ID is: ${recipefull.meals[0].strMeal}")
                Log.d("RecipeActivity", "SASTOJAK is: ${recipefull.meals[0].strIngredient1}")
                /*
                runOnUiThread {
                    recyclerView_home.adapter = RecipeFull(recipefull)
                }
                */
                val listaSastojaka= mutableListOf<String>()
                val listaKolicina= mutableListOf<String>()

                listaSastojaka.add(recipefull.meals[0].strIngredient1)
                listaSastojaka.add(recipefull.meals[0].strIngredient2)
                listaSastojaka.add(recipefull.meals[0].strIngredient3)
                listaSastojaka.add(recipefull.meals[0].strIngredient4)
                listaSastojaka.add(recipefull.meals[0].strIngredient5)
                listaSastojaka.add(recipefull.meals[0].strIngredient6)
                listaSastojaka.add(recipefull.meals[0].strIngredient7)
                listaSastojaka.add(recipefull.meals[0].strIngredient8)
                listaSastojaka.add(recipefull.meals[0].strIngredient9)
                listaSastojaka.add(recipefull.meals[0].strIngredient10)
                listaSastojaka.add(recipefull.meals[0].strIngredient11)
                listaSastojaka.add(recipefull.meals[0].strIngredient12)
                listaSastojaka.add(recipefull.meals[0].strIngredient13)
                listaSastojaka.add(recipefull.meals[0].strIngredient14)
                listaSastojaka.add(recipefull.meals[0].strIngredient15)
                listaSastojaka.add(recipefull.meals[0].strIngredient16)
                listaSastojaka.add(recipefull.meals[0].strIngredient17)
                listaSastojaka.add(recipefull.meals[0].strIngredient18)
                listaSastojaka.add(recipefull.meals[0].strIngredient19)
                listaSastojaka.add(recipefull.meals[0].strIngredient20)

                listaKolicina.add(recipefull.meals[0].strMeasure1)
                listaKolicina.add(recipefull.meals[0].strMeasure2)
                listaKolicina.add(recipefull.meals[0].strMeasure3)
                listaKolicina.add(recipefull.meals[0].strMeasure4)
                listaKolicina.add(recipefull.meals[0].strMeasure5)
                listaKolicina.add(recipefull.meals[0].strMeasure6)
                listaKolicina.add(recipefull.meals[0].strMeasure7)
                listaKolicina.add(recipefull.meals[0].strMeasure8)
                listaKolicina.add(recipefull.meals[0].strMeasure9)
                listaKolicina.add(recipefull.meals[0].strMeasure10)
                listaKolicina.add(recipefull.meals[0].strMeasure11)
                listaKolicina.add(recipefull.meals[0].strMeasure12)
                listaKolicina.add(recipefull.meals[0].strMeasure13)
                listaKolicina.add(recipefull.meals[0].strMeasure14)
                listaKolicina.add(recipefull.meals[0].strMeasure15)
                listaKolicina.add(recipefull.meals[0].strMeasure16)
                listaKolicina.add(recipefull.meals[0].strMeasure17)
                listaKolicina.add(recipefull.meals[0].strMeasure18)
                listaKolicina.add(recipefull.meals[0].strMeasure19)
                listaKolicina.add(recipefull.meals[0].strMeasure20)
                val finalList= mutableListOf<String>()
                for(i in 0..19){
                    if(listaSastojaka[i]!=null && listaSastojaka[i]!=""){
                        finalList.add("${listaSastojaka[i]}"+" - "+"${listaKolicina[i]}")
                    }

                }

                Log.d("RecipeActivity", "LISTA is: ${listaSastojaka.toString()}")
                runOnUiThread {
                    recipeTitle_textView.text=recipefull.meals[0].strMeal.toString()
                    ingredients_textView.text=finalList.toString()
                    recipeText_textView.text=recipefull.meals[0].strInstructions.toString()
                    val recipeImage = recipefull.meals[0].strMealThumb.toString()
                    Picasso.get().load("$recipeImage").into(recipe_imageView)
                }

            }
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }
}

class RecipeFull(val meals: List<RecipeView>)
class RecipeView(val strMeal:String, val strMealThumb:String, val idMeal:String, val strInstructions:String,
                 val strIngredient1: String, val strIngredient2: String, val strIngredient3: String, val strIngredient4: String,
                 val strIngredient5: String, val strIngredient6: String,val strIngredient7: String,val strIngredient8: String,
                 val strIngredient9: String,val strIngredient10: String,val strIngredient11: String,val strIngredient12: String,
                 val strIngredient13: String,val strIngredient14: String,val strIngredient15: String,val strIngredient16: String,
                 val strIngredient17: String,val strIngredient18: String,val strIngredient19: String,val strIngredient20: String,
                 val strMeasure1: String, val strMeasure2: String, val strMeasure3: String, val strMeasure4: String, val strMeasure5: String,
                 val strMeasure6: String,val strMeasure7: String,val strMeasure8: String,val strMeasure9: String,val strMeasure10: String,
                 val strMeasure11: String,val strMeasure12: String,val strMeasure13: String,val strMeasure14: String,val strMeasure15: String,
                 val strMeasure16: String,val strMeasure17: String,val strMeasure18: String,val strMeasure19: String,val strMeasure20: String)

/*
class StrIngredient(val strIngredient1: String, val strIngredient2: String, val strIngredient3: String, val strIngredient4: String,
                    val strIngredient5: String, val strIngredient6: String,val strIngredient7: String,val strIngredient8: String,
                    val strIngredient9: String,val strIngredient10: String,val strIngredient11: String,val strIngredient12: String,
                    val strIngredient13: String,val strIngredient14: String,val strIngredient15: String,val strIngredient16: String,
                    val strIngredient17: String,val strIngredient18: String,val strIngredient19: String,val strIngredient20: String)


class StrMeasure(val strMeasure1: String, val strMeasure2: String, val strMeasure3: String, val strMeasure4: String, val strMeasure5: String,
                 val strMeasure6: String,val strMeasure7: String,val strMeasure8: String,val strMeasure9: String,val strMeasure10: String,
                 val strMeasure11: String,val strMeasure12: String,val strMeasure13: String,val strMeasure14: String,val strMeasure15: String,
                 val strMeasure16: String,val strMeasure17: String,val strMeasure18: String,val strMeasure19: String,val strMeasure20: String)


 */

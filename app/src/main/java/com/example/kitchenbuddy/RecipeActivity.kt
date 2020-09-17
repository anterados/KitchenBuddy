package com.example.kitchenbuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_recipe.*
import okhttp3.*
import java.io.IOException


class RecipeActivity:AppCompatActivity() {

    //val recipeId = intent.getSerializableExtra("id") as String
    var imgString:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recipeId = intent.getSerializableExtra("id") as String
        Log.d("RecipeActivity", "ID is: $recipeId")
        setContentView(R.layout.activity_recipe)
        fetchRating()
        fetchJson(recipeId)
        //recipeTitle_textView.text=recipeId.toString()
        findFav(recipeId)
        findStore_button.setOnClickListener {
            val intent = Intent(this, FindStoresActivity::class.java)
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            //ActivityCompat.finishAffinity(this)  or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        button_rate.setOnClickListener {
            uploadRating()



        }
        button_favorite.setOnClickListener {
            val res=button_favorite.resources

            Log.d("RecipeActivity", "ID is: ${res.toString()}")
            if(button_favorite.text.toString()=="0"){
                setRecipeFavorite(recipeId)
                button_favorite.setBackgroundResource(R.drawable.ic_favorite_red)
                Toast.makeText(this, "Added to favorites!", Toast.LENGTH_SHORT).show()
            }

            else if(button_favorite.text.toString()=="1"){
                removeRecipeFavorite(recipeId)
                button_favorite.setBackgroundResource(R.drawable.ic_favorite)
                Toast.makeText(this, "Removed from favorites!", Toast.LENGTH_SHORT).show()

        }

        }

        Log.d("RecipeActivity", "${button_favorite.text.toString()} je vrijednost texta!")
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
                        finalList.add("\n${listaSastojaka[i]}"+" - "+"${listaKolicina[i]}")
                    }

                }

                Log.d("RecipeActivity", "LISTA is: ${listaSastojaka.toString()}")
                runOnUiThread {
                    recipeTitle_textView.text=recipefull.meals[0].strMeal.toString()

                    ingredients_textView.text=finalList.toString().removePrefix("[").removeSuffix("]")
                    recipeText_textView.text=recipefull.meals[0].strInstructions.toString()
                    val recipeImage = recipefull.meals[0].strMealThumb.toString()
                    imgString=recipefull.meals[0].strMealThumb.toString()
                    Picasso.get().load("$recipeImage").into(recipe_imageView)
                }

            }
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun uploadRating(){
        val uid =FirebaseAuth.getInstance().uid ?:""
        val recipeId =  intent.getSerializableExtra("id") as String
        val ref = FirebaseDatabase.getInstance().getReference("/RecipeRating/$recipeId/$uid")
        val ratingNum:Float= ratingBar.rating
        Log.d("RecipeActivity", "POSLANO ${ratingNum.toString()}")
        val rating=RecipesRating(recipeId,ratingNum)

        ref.setValue(rating)
            .addOnSuccessListener {
                Log.d("RecipeActivity", "PISANJE U BAZU")
            }
    }

    private fun fetchRating(){
        var returnValue:Float=0F
        val uid =FirebaseAuth.getInstance().uid ?:""
        val recipeId =  intent.getSerializableExtra("id") as String
        val ref2 = FirebaseDatabase.getInstance().getReference().child("RecipeRating").child("$recipeId")//("/RecipeRating/$recipeId/")
        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var total:Float = 0F
                var count= 0
                Log.d("RecipeActivity", "DIJETE!!!!!!!!!!! ${dataSnapshot.toString()}")
                runOnUiThread {
                    for (child in dataSnapshot.children) {
                        Log.d("RecipeActivity", "DIJETE2!!!!!!!!!!! ${child.value.toString()}")
                        var mySubString = child.value.toString().substring(
                           child.value.toString().indexOf("=") + 1,
                            child.value.toString().indexOf(",")
                        )
                        Log.d("RecipeActivity", "String!!!!!!!!!!! ${mySubString.toString()}")
                        val number = mySubString.toFloat()

                        Log.d("RecipeActivity", "String!!!!!!!!!!! ${number.toString()}")

                        total = total + number!!
                        count = count + 1

                    }
                }
                //ratingBar.rating=total.toFloat()
                returnValue=total/count
                ratingBar.rating=returnValue
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException() // don't ignore errors
                Log.d("RecipeActivity", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }
    fun setRecipeFavorite(recipe_id: String){
        val uid =FirebaseAuth.getInstance().uid ?:""
        val ref1 = FirebaseDatabase.getInstance().getReference("/users/$uid/$recipe_id")
        val favorite=FavRecipe(recipeTitle_textView.text.toString(),recipe_id, imgString)
        Log.d("MainActivity", "KONTROLA!")


        ref1.setValue(favorite)
            .addOnSuccessListener {
                Log.d("MainActivity", "PISANJE U BAZU")
            }


    }
    fun removeRecipeFavorite(recipe_id: String){
        val uid =FirebaseAuth.getInstance().uid ?:""
        val ref1 = FirebaseDatabase.getInstance().getReference("/users/$uid/$recipe_id")
        //val ref2 = FirebaseDatabase.getInstance().getReference("/users/$uid")
        //val favorite=FavRecipe(recipeTitle_textView.text.toString(),recipe_id, imgString)
        Log.d("MainActivity", "KONTROLA!")


        ref1.removeValue()
            .addOnSuccessListener {
                Log.d("MainActivity", "BRISANJE U BAZU")
            }


    }
    fun findFav(recipe_id: String){
        val uid =FirebaseAuth.getInstance().uid ?:""
        val ref1 = FirebaseDatabase.getInstance().getReference("/users/$uid/$recipe_id")
        ref1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var post = dataSnapshot.getValue()


                if (post==null){
                    button_favorite.text="0"
                }
                if(post.toString().contains("title")){
                    button_favorite.text="1"
                    button_favorite.setBackgroundResource(R.drawable.ic_favorite_red)
                }


            }
            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException() // don't ignore errors
                Log.d("RecipeActivity", "loadPost:onCancelled", databaseError.toException())
            }
        })

    }
}
class FavRecipe(val title:String, val id:String, val image:String)

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

class RecipesRating(val recID:String, val rating:Float)
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

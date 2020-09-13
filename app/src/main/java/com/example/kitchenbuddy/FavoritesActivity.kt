package com.example.kitchenbuddy

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_favorites.*
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*

class FavoritesActivity:AppCompatActivity(){
    private var emptyData: TextView? = null
    //private var favList:MutableList<RecipeFav> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        recyclerView_favorites.layoutManager=LinearLayoutManager(this)
        emptyData = findViewById(R.id.empty_view_fav) as TextView
        fetchRating()

        //Log.d("RecipeActivity", "LISTA!!!!!!!!!!! ${favList.toString()}")
        /*
        runOnUiThread {
            recyclerView_favorites.adapter = FavoritesAdapter(favList)
        }

         */
    }

    private fun fetchRating(){

        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref2 = FirebaseDatabase.getInstance().getReference().child("users").child("$uid")
        ReadData(object: FirebaseCallback {
            override fun onCallback(lista: MutableList<RecipeFav>) {
                Log.d("RecipeActivity", "PROBA!!!!!!!!!!! ${lista.toString()}")
                if(lista.isNullOrEmpty()){
                    emptyData!!.text="You have no favorites yet! :("
                    Toast.makeText(this@FavoritesActivity, "You have no favorites yet! :(", Toast.LENGTH_SHORT).show()
                }
                else {
                    runOnUiThread {
                        emptyData!!.setVisibility(View.GONE)
                        recyclerView_favorites.adapter = FavoritesAdapter(lista)
                    }
                }
            }
        },ref2)


    }

    
    private fun ReadData(firebaseCallback: FirebaseCallback, ref:DatabaseReference) {
        var fetchList:MutableList<RecipeFav> = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener {
            public override fun onDataChange(dataSnapshot: DataSnapshot) {
                var i=0;
                var tit=""
                var img=""
                Log.d("RecipeActivity", "DIJETE!!!!!!!!!!! ${dataSnapshot.toString()}")

                for (child in dataSnapshot.children) {
                    Log.d("RecipeActivity", "DIJETE2!!!!!!!!!!! ${child.value.toString()}")
                    val title = dataSnapshot.child("title")
                       .getValue(String::class.java)//!! //child("rating"). Float::class.java
                    Log.d("RecipeActivity", "TITLEEEEE!!!!!!!!!!! ${title.toString()}")
                    var imagestring = child.value.toString().substring(
                        child.value.toString().indexOf("image="),
                        child.value.toString().indexOf(",")
                    )
                    var titlestring = child.value.toString().substring(
                        child.value.toString().indexOf("title="),
                        child.value.toString().indexOf("}")
                    )
                    //Log.d("RecipeActivity", "naslov!!!!!!!!!!! ${titlestring.removePrefix("title=").toString()}")
                    //Log.d("RecipeActivity", "slika!!!!!!!!!!! ${imagestring.removePrefix("image=").toString()}")
                    tit=titlestring.removePrefix("title=").toString()
                    img=imagestring.removePrefix("image=").toString()
                    Log.d("RecipeActivity", "TIT!!!!!!!!!!! ${tit.toString()}")
                    Log.d("RecipeActivity", "IMG!!!!!!!!!!! ${img.toString()}")
                    fetchList.add(RecipeFav(tit, img))

                    //fetchList[i].title_fav=titlestring.removePrefix("title=").toString()
                    //fetchList[i].image_fav=imagestring.removePrefix("image=").toString()
                    Log.d("RecipeActivity", "lista!!!!!!!!!!! ${ fetchList[i].title_fav.toString()}")
                    Log.d("RecipeActivity", "lista!!!!!!!!!!! ${ fetchList[i].image_fav.toString()}")
                    i += 1

                }
                Log.d("RecipeActivity", "lista unutra!!!!!!!!!!! ${ fetchList.toString()}")
                firebaseCallback.onCallback(fetchList)


            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException() // don't ignore errors
                Log.d("RecipeActivity", "loadPost:onCancelled", databaseError.toException())
            }
        })
        Log.d("RecipeActivity", "lista unutra!!!!!!!!!!! ${ fetchList.toString()}")
        
    }
   private interface FirebaseCallback{
       fun onCallback(lista :MutableList<RecipeFav>)
   }



}

class FavList(val meals: List<RecipeFav>)
class RecipeFav(var title_fav:String, var image_fav:String)
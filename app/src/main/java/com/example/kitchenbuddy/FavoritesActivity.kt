package com.example.kitchenbuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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

        fetchFavorites()

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
                Toast.makeText(this, "You are there already! :)", Toast.LENGTH_SHORT).show()
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

    private fun fetchFavorites(){

        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref2 = FirebaseDatabase.getInstance().getReference().child("users").child("$uid")
        ReadData(object: FirebaseCallback {
            override fun onCallback(lista: MutableList<RecipeFav>) {
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
                var id=""
                for (child in dataSnapshot.children) {

                    if(child.value.toString().contains("image")) {
                        var imagestring = child.value.toString().substring(
                            child.value.toString().indexOf("image="),
                            child.value.toString().indexOf(",")
                        )
                        var idstring = child.value.toString().substring(
                            child.value.toString().indexOf("id="),
                            child.value.toString().indexOf(", title")
                        )
                        var titlestring = child.value.toString().substring(
                            child.value.toString().indexOf("title="),
                            child.value.toString().indexOf("}")
                        )

                        id = idstring.removePrefix("id=").toString()
                        tit = titlestring.removePrefix("title=").toString()
                        img = imagestring.removePrefix("image=").toString()
                        Log.d("RecipeActivity", "ID!!!!!!!!!!! ${id.toString()}")
                        Log.d("RecipeActivity", "TIT!!!!!!!!!!! ${tit.toString()}")
                        Log.d("RecipeActivity", "IMG!!!!!!!!!!! ${img.toString()}")
                        fetchList.add(RecipeFav(tit, img, id))
                    }

                }
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
class RecipeFav(var title_fav:String, var image_fav:String, var recipe_id:String)
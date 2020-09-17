package com.example.kitchenbuddy

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.recipe_row.view.*

class FavoritesAdapter(val favList: MutableList<RecipeFav>):RecyclerView.Adapter<FavoriteHolder>() {

    var outer:Float=0F;
    override fun getItemCount(): Int {
        return favList.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {


        Log.d("HomeAdapter","${parent.toString()}" +"je RODIOC")
        Log.d("HomeAdapter","${viewType.toString()}" +"je POGLED")
        val layoutInflater=LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.recipe_row, parent, false)

        return FavoriteHolder(cellForRow)

    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        val recipe=favList.get(position)
        holder.view.textView_recipe_title.text = recipe.title_fav
        //holder.view.textView_recipe_ingredients.text = recipe.strIngredient1

        val thumbnailImage=holder.view.imageView_recipe_thumbnail
        Picasso.get().load(recipe.image_fav).into(thumbnailImage)
        val rating=fetchRating(recipe.recipe_id,holder,position)
        //ubaciti rating******
        Log.d("HomeAdapter","${outer.toString()}" +"je RATING")


        holder.view.setOnClickListener {
            Log.d("HomeAdapter","${recipe.recipe_id}" +"je ID!!!!!!!!!!(prvi)!!!!!!!!!")
            val context=holder.view.context
            val intent = Intent(context,RecipeActivity::class.java)
            intent.putExtra("id",recipe.recipe_id)
            Log.d("Favorites","${recipe.recipe_id}" +"je ID!!!!!!!!!!!!!!!!!!!")

            context.startActivity(intent)
        }

    }
    fun fetchRating(id:String,holder: FavoriteHolder,position: Int){
        val uid = FirebaseAuth.getInstance().uid ?:""
        val recipeId =  id
        var returnValue:Float=0F
        val ref2 = FirebaseDatabase.getInstance().getReference().child("RecipeRating").child("$recipeId")//("/RecipeRating/$recipeId/")
        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var total:Float = 0F
                var count= 0

                for (child in dataSnapshot.children) {

                    var mySubString = child.value.toString().substring(
                        child.value.toString().indexOf("=") + 1,
                        child.value.toString().indexOf(",")
                    )

                    val number = mySubString.toFloat()



                    total = total + number!!
                    count = count + 1

                }

                //ratingBar.rating=total.toFloat()
                Log.d("HomeActivity", "TOTAL!!!!!!!!!!! ${total.toString()}")
                returnValue=total/count
                Log.d("HomeActivity", "Povratna!!!!!!!!!!! ${returnValue.toString()}")
                //returnResult(returnValue)
                //this@HomeAdapter.outer=returnValue
                if(returnValue>0){
                    holder.view.ratingBar3.visibility=View.VISIBLE
                    holder.view.ratingBar3.rating=returnValue
                }
                else
                    holder.view.textView_recipe_ingredients.text="Not rated yet!"
            }


            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException() // don't ignore errors
                Log.d("RecipeActivity", "loadPost:onCancelled", databaseError.toException())
            }
        })
        //Log.d("HomeActivity", "TOTAL!!!!!!!!!!! ${total.toString()}")
        Log.d("HomeActivity", "POVRATNA!!!!!!!!!!! ${returnValue.toString()}")


    }





}

class FavoriteHolder(val view:View):RecyclerView.ViewHolder(view){
}
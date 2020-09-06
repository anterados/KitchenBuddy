package com.example.kitchenbuddy

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import com.squareup.picasso.Picasso
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recipe_row.view.*

class IngredientAdapter(val ingredientList: IngredientActivity.IngredientList, private val callbackInterface: CallbackInterface):RecyclerView.Adapter<IngredientHolder>()   {
    override fun getItemCount(): Int {

        return ingredientList.meals.count()
        Log.v("IngredientActivity", "${ingredientList.meals.count()}")
        //return 10

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientHolder {

        val layoutInflater=LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.recipe_row, parent, false)
        /* prvi način
        cellForRow.setOnClickListener {
            //val intent = Intent(parent?.context, HomeActivity::class.java)
            //parent?.context.startActivity(intent)

        }*/

        return IngredientHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: IngredientHolder, position: Int) {
        val recipe=ingredientList.meals.get(position)
        holder.view.textView_recipe_title.text = recipe.strIngredient
    //drugi način
        holder.itemView.setOnClickListener {
            //val intent = Intent(it.context, HomeActivity::class.java)
            //it.context.startActivity(intent)
            //listener(recipe.strIngredient)
        callbackInterface.passDataCallback(recipe.strIngredient)

        }

        val thumbnailImage=holder.view.imageView_recipe_thumbnail
        Picasso.get().load("https://www.themealdb.com/images/ingredients/"+ (recipe.strIngredient) + ".png").into(thumbnailImage)
    }

}



class IngredientHolder(val view:View):RecyclerView.ViewHolder(view){

}
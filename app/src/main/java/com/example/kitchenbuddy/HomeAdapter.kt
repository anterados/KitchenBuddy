package com.example.kitchenbuddy

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recipe_row.view.*

class HomeAdapter(val homeList: HomeList):RecyclerView.Adapter<RecipeHolder>() {
    override fun getItemCount(): Int {
            return homeList.meals.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {

        val layoutInflater=LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.recipe_row, parent, false)
        return RecipeHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {

        val recipe=homeList.meals.get(position)
        holder.view.textView_recipe_title.text = recipe.strMeal
        //holder.view.textView_recipe_ingredients.text = recipe.strIngredient1

        val thumbnailImage=holder.view.imageView_recipe_thumbnail
        Picasso.get().load(recipe.strMealThumb).into(thumbnailImage)

        holder.view.setOnClickListener {
            Log.d("HomeAdapter","${recipe.idMeal}" +"je ID!!!!!!!!!!(prvi)!!!!!!!!!")
            val context=holder.view.context
            val intent = Intent(context,RecipeActivity::class.java)
            intent.putExtra("id",recipe.idMeal)
            Log.d("HomeAdapter","${recipe.idMeal}" +"je ID!!!!!!!!!!!!!!!!!!!")
            context.startActivity(intent)
        }
    }

}

class RecipeHolder(val view:View):RecyclerView.ViewHolder(view){

}
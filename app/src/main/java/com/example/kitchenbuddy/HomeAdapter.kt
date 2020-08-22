package com.example.kitchenbuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recipe_row.view.*

class HomeAdapter(val homeList: HomeList):RecyclerView.Adapter<RecipeHolder>() {
    override fun getItemCount(): Int {
        return homeList.results.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {

        val layoutInflater=LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.recipe_row, parent, false)
        return RecipeHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        val recipe=homeList.results.get(position)
        holder.view.textView_recipe_title.text = recipe.title
    }
}

class RecipeHolder(val view:View):RecyclerView.ViewHolder(view){

}
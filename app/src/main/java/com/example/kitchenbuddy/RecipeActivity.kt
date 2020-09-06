package com.example.kitchenbuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.recipe_row.*

class RecipeActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recipeId = intent.getSerializableExtra("id") as String
        Log.d("RecipeActivity", "ID is: $recipeId")
        setContentView(R.layout.activity_recipe)

        recipeTitle_textView.text=recipeId.toString()


    }
}
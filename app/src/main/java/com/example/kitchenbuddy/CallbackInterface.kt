package com.example.kitchenbuddy

interface CallbackInterface {
    fun passDataCallback(message: String){}
    fun favoritesCallback(data:MutableList<RecipeFav>):MutableList<RecipeFav>{
        return data
    }

}


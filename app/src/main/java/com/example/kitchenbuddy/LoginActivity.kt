package com.example.kitchenbuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity:AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sign_in_button_login.setOnClickListener {
            val username = username_edittext_login.text.toString()
            val password= password_edittext_login.text.toString()

            if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Username and/or password empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("Login", "Sign in attempt: $username + $password !")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    Log.d("Login", "Signed in! UID = ${it.result!!.user!!.uid}")

                    val intent = Intent(this, IngredientActivity::class.java)
                    startActivity(intent)
                }

                .addOnFailureListener {
                    Log.d("Login", "Failed to sign in: ${it.message}")
                    Toast.makeText(this, "Failed to sign in: ${it.message}", Toast.LENGTH_SHORT).show()
                }


        }

        not_registered_textview_login.setOnClickListener {
            finish()
        }

    }

}
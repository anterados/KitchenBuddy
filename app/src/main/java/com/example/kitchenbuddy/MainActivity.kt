package com.example.kitchenbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button_register.setOnClickListener {

            val email = email_edittext_register.text.toString()
            val password = password_edittext_register.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Username and/or password empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("MainActivity", "Email is: " + email)
            Log.d("MainActivity", "Password is: $password")

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    Log.d("Main", "User created! UID = ${it.result!!.user!!.uid}")
                }
                .addOnFailureListener {
                    Log.d("Main", "Failed to create user: ${it.message}")
                    Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        already_registered_textview_register.setOnClickListener {
            Log.d("MainActivity", "Hopa Cupa!")

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}
package com.example.kitchenbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button_register.setOnClickListener {

            val email = email_edittext_register.text.toString()
            val password = password_edittext_register.text.toString()
            val username=username_edittext_register.text.toString()

            if(email.isEmpty() || password.isEmpty() || username.isEmpty()){
                Toast.makeText(this, "Something's missing!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("MainActivity", "Email is: " + email)
            Log.d("MainActivity", "Password is: $password")

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    Log.d("Main", "User created! UID = ${it.result!!.user!!.uid}")
                    uploadUsername()
                }
                .addOnFailureListener {
                    Log.d("Main", "Failed to create user: ${it.message}")
                    Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                }

                .addOnSuccessListener {
                    Toast.makeText(this, "Congratulations! Proceed to sign in.", Toast.LENGTH_SHORT).show()
                    uploadUsername()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }

        }

        already_registered_textview_register.setOnClickListener {
            Log.d("MainActivity", "Hopa Cupa!")

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }
    //private lateinit var database: DatabaseReference

    private fun uploadUsername(){
        val uid =FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user=User(uid, email_edittext_register.text.toString(), username_edittext_register.text.toString())
        Log.d("MainActivity", "KONTROLA!")
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("MainActivity", "PISANJE U BAZU")
            }
    }
}
class User(val uid:String,val email:String, val username:String)
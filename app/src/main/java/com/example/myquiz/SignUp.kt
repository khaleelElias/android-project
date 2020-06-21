package com.example.myquiz

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()
    lateinit var mDatabase : DatabaseReference
    val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        mDatabase = FirebaseDatabase.getInstance().getReference("Names")
        val signUpButton = findViewById<Button>(R.id.signUp)
        signUpButton.setOnClickListener{ v->
            register()
        }
    }

    //creating a account
    private fun register() {
        var pd= ProgressDialog(this)
        pd.setMessage(getString(R.string.pls_wait))
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)

        val emailText = findViewById<EditText>(R.id.emailText)
        val userNameText = findViewById<EditText>(R.id.userNameText)
        val passWordText = findViewById<EditText>(R.id.passWordText)

        var signEmail = emailText.text.toString()
        var userName = userNameText.text.toString()
        var signPassWord = passWordText.text.toString()





        if(signEmail.isNotEmpty() && userName.isNotEmpty() && signPassWord.isNotEmpty()){
            if(userName.length > 6 && signPassWord.length > 8 && signEmail.contains("@") ) {
                pd.show()
                mAuth.createUserWithEmailAndPassword(signEmail, signPassWord)
                    .addOnCompleteListener(this, OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = mAuth.currentUser
                            val uid = user!!.uid

                            val userValue = hashMapOf(
                                "name" to userName,
                                "Admin" to false
                            )

                            //mDatabase.child(uid).child("Names").setValue(userName)
                            db.collection("users").document(uid).set(userValue)
                                .addOnSuccessListener {
                                    startActivity(Intent(this, StartPage::class.java))
                                    Toast.makeText(
                                        this,
                                        getString(R.string.Successfully_registered),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }.addOnFailureListener { e ->
                                    Toast.makeText(
                                        this,
                                        getString(R.string.Error_registering),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                        } else {
                            Toast.makeText(
                                this,
                                getString(R.string.Error_registering),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    })
            }else
                 Toast.makeText(this,getString(R.string.valid_email), Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,getString(R.string.fill_the_field), Toast.LENGTH_LONG).show()
        }

    }
}
package com.example.myquiz

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myquiz.R.id.loginButton
import com.example.myquiz.R.id.progress_circular
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class  Login : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        val loginButton = findViewById<Button>(loginButton)
        loginButton.setOnClickListener{ v->
            login()
        }
    }


    //log in to the app
    private fun login(){
        var pd= ProgressDialog(this)
        pd.setMessage(getString(R.string.pls_wait))
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        val emailTextField = findViewById<EditText>(R.id.emailTextField)
        val passwordTextField = findViewById<EditText>(R.id.passwordTextField)

        var email = emailTextField.text.toString()
        var password = passwordTextField.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()){
                pd.show()
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            pd.hide()
                            startActivity(Intent(this, StartPage::class.java))
                            Toast.makeText(
                                this,
                                getString(R.string.Success_loggedin),
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            pd.hide()
                            Toast.makeText(this, getString(R.string.Wrong_em_pw), Toast.LENGTH_LONG).show()
                        }
                    })
        } else {
            pd.hide()
            Toast.makeText(this, getString(R.string.fill_the_field), Toast.LENGTH_SHORT).show()}
    }
}

package com.example.myquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //login button
        val loginMainButton = findViewById<Button>(R.id.loginMain)
        //singUp button
        val singUpMain = findViewById<Button>(R.id.signupMain)

        loginMainButton.setOnClickListener{v->
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        singUpMain.setOnClickListener{v->
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

    }
}


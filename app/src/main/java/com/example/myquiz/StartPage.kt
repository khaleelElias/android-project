package com.example.myquiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore

class StartPage : AppCompatActivity() {

    lateinit var mDatabase : DatabaseReference
    val db = FirebaseFirestore.getInstance()
    var user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_page)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)


        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_askQuetions
            )
        )

/*
var uid = user!!.uid

db.collection("users").document(uid).get().addOnSuccessListener { document ->
    nameTxt.text = "Welcome " + document["name"]
}
    .addOnFailureListener { exception ->
        Log.w("startPage", "Error getting documents.", exception)
    }

/*mDatabase = FirebaseDatabase.getInstance().getReference("Names")

mDatabase.child(uid).child("Names").addValueEventListener(object : ValueEventListener {
    override fun onCancelled(p0: DatabaseError) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun onDataChange(snapshot: DataSnapshot) {
        val result = snapshot.child("Names").toString()
        nameTxt.text = "Welcome" + result
    }
})
*/*/
setupActionBarWithNavController(navController, appBarConfiguration)
navView.setupWithNavController(navController)
}
}

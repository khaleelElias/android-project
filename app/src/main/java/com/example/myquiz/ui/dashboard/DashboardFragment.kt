package com.example.myquiz.ui.dashboard

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.myquiz.*
import com.example.myquiz.R.layout.fragment_dashboard_start
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private val mAuth = FirebaseAuth.getInstance()
    private val myRef = FirebaseDatabase.getInstance().getReference("scoreBoard")
    private lateinit var scores:TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(fragment_dashboard_start, container, false)
        val start = root.findViewById<Button>(R.id.startQuiz_button)
        scores = root.findViewById(R.id.myScores)
        //start the Quiz when you press the button
        start.setOnClickListener{
            val intent = Intent(this.context, StartQuiz::class.java)
            startActivity(intent)
        }


        readScores()
        return root

    }


    //fetching user score from firebase
    private fun readScores(){
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {

                scores.setText((scores.text.toString() + "\n\n" + dataSnapshot.value).toString())

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                 }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, getString(R.string.Failed_to_load_comments),
                    Toast.LENGTH_SHORT).show()
            }
        }
        myRef.child(mAuth.currentUser?.uid.toString()).addChildEventListener(childEventListener)

    }





}
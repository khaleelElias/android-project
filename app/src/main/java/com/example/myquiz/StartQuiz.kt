package com.example.myquiz

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class StartQuiz : AppCompatActivity() {

    private var questions = mutableListOf<Question>()
    val db = FirebaseFirestore.getInstance()
    val mAuth = FirebaseAuth.getInstance()
    val myRef = FirebaseDatabase.getInstance().getReference("scoreBoard")

    private var points = 0
    private var questionCounter = 0
    private var answerPressed = false

    lateinit var button1:Button
    lateinit var button2:Button
    lateinit var button3:Button
    lateinit var button4:Button
    lateinit var questionCounterTextView:TextView
    lateinit var scoreCounterTextView:TextView
    lateinit var textView_question:TextView
    lateinit var next_button:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_dashboard)
        button1 = findViewById<Button>(R.id.button1)
        button2 = findViewById<Button>(R.id.button2)
        button3 = findViewById<Button>(R.id.button3)
        button4 = findViewById<Button>(R.id.button4)
        textView_question=findViewById<TextView>(R.id.textView_question)
        next_button = findViewById<Button>(R.id.next_button)
        questionCounterTextView = findViewById(R.id.questionCounter)
        scoreCounterTextView = findViewById(R.id.scoreCounter)


        readData()
        option1Tapped()
        option2Tapped()
        option3Tapped()
        option4Tapped()
        nextTapped()
    }

    //fetching the questions from the firebase
    fun readData(){
        db.collection("Questions").get().addOnSuccessListener { result ->
            for(document in result){
                questions.add(Question(
                    document.get("Answer1") as String,
                    document.get("Answer2") as String,
                    document.get("Answer3") as String,
                    document.get("Answer4") as String,
                    document.get("Question") as String,
                    document.get("RightAnswer") as String
                ))
            }

            questionCounterTextView.setText((this.questionCounter + 1).toString() + "/" + questions.size)
            scoreCounterTextView.setText(this.points.toString())

            setButtons()
            updateGame()
        }
    }
    //updating
    fun updateGame() {

        textView_question.text = questions[questionCounter].Question
        button1.text = questions[questionCounter].Answer1
        button2.text = questions[questionCounter].Answer2
        button3.text = questions[questionCounter].Answer3
        button4.text = questions[questionCounter].Answer4


    }
    //Enable the buttons after first click
    fun setButtons(){
        button1.isEnabled = !this.answerPressed;
        button2.isEnabled = !this.answerPressed;
        button3.isEnabled = !this.answerPressed;
        button4.isEnabled = !this.answerPressed;
    }

    fun option1Tapped(){
        button1.setOnClickListener(){
            validateAnswer(button1)
        }
    }

    fun option2Tapped(){

        button2.setOnClickListener(){
            validateAnswer(button2)
        }
    }

    fun option3Tapped(){

        button3.setOnClickListener() {
            validateAnswer(button3)
        }

    }

    fun option4Tapped(){

        button4.setOnClickListener(){
            validateAnswer(button4)
        }

    }
    //checking the right answer
    private fun validateAnswer(button:Button){
        this.answerPressed = true;
        setButtons();
        if (button.text == questions[questionCounter].RightAnswer ){
            button.setBackgroundColor(Color.parseColor("#0000FF"))
            points++;
        }else {
            //yellow is wrong answer
            button.setBackgroundColor(Color.parseColor("#FFFF00"))
            vibrate()

        }

        questionCounterTextView.setText((this.questionCounter + 1).toString() + "/" + questions.size)
        scoreCounterTextView.setText(this.points.toString())


    }
    //next question
    fun nextTapped(){
        next_button.setOnClickListener(){

            if(questionCounter == questions.size - 1){
                //ended game
                val item = myRef.child(mAuth.currentUser?.uid.toString()).push()
                item.setValue(this.points)
                finish()

            }else{

                this.answerPressed = false;
                setButtons();
                questionCounter++
                updateGame()
                button1.setBackgroundColor(Color.parseColor("#FFFFFF"))
                button2.setBackgroundColor(Color.parseColor("#FFFFFF"))
                button3.setBackgroundColor(Color.parseColor("#FFFFFF"))
                button4.setBackgroundColor(Color.parseColor("#FFFFFF"))

            }

        }

    }

    fun Context.vibrate(milliseconds:Long = 500){
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Check whether device/hardware has a vibrator
        val canVibrate:Boolean = vibrator.hasVibrator()

        if(canVibrate){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                // void vibrate (VibrationEffect vibe)
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        milliseconds,
                        // The default vibration strength of the device.
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }else{
                vibrator.vibrate(milliseconds)
            }
        }
    }

    val Context.hasVibrator:Boolean
        get() {
            val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            return vibrator.hasVibrator()
        }


}
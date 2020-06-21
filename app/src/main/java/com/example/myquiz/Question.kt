package com.example.myquiz

public class Question {
    var Answer1: String? = null
    var Answer2: String? = null
    var Answer3: String? = null
    var Answer4: String? = null
    var Question: String? = null
    var RightAnswer: String? = null

    constructor(A1:String, A2:String, A3: String, A4: String, Q:String, R:String){
        this.Answer1 = A1
        this.Answer2 = A2
        this.Answer3 = A3
        this.Answer4 = A4
        this.Question = Q
        this.RightAnswer = R
    }
}


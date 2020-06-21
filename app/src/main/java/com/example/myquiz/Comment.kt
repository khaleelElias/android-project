package com.example.myquiz

class Comment {
    companion object Factory {
        fun create(): Comment = Comment()
    }
    var objectId: String? = null
    var itemText: String? = null
    var response: String? = null
}

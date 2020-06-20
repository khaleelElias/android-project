package com.example.myquiz

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.myquiz.ui.AskQuestions.AskQuestionsFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_home.*

class CustomListViewAdapter(context: Context, CommentList: MutableList<Comment>) : BaseAdapter() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var itemList = CommentList
    private val storageRef = Firebase.storage.getReference("images")
    val db = FirebaseFirestore.getInstance()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val objectId: String = itemList.get(position).objectId as String
        val itemText: String = itemList.get(position).itemText as String
        val response: String = itemList.get(position).response as String
        val ownerId: String? = itemList.get(position).ownerId as String

        val view: View
        val vh: ListRowHolder

        if (convertView == null) {
            view = mInflater.inflate(R.layout.row_item, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }


        vh.question.text = itemText
        vh.response.text = response
        vh.id = objectId
        if (ownerId != null) {
            getImage(ownerId, vh.image)
            getUserName(ownerId, vh.username)
        }
        //vh.ownerId = ownerId

        return view
    }
    //getting the Questions
    override fun getItem(index: Int): Any {
        return itemList.get(index)
    }
    //getting the question by Id
    override fun getItemId(index: Int): Long {
        return index.toLong()
    }
    //getting the counter by it's size
    override fun getCount(): Int {
        return itemList.size
    }

    private fun getUserName(uid:String, username: TextView) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    //Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    username.text = document.data?.get("name") as String
                }
            }
    }

    private fun getImage(uid:String, image_view:ImageView) {
        val ref = storageRef.child(uid)
        val data: Long = 1024 * 1024;
        ref.getBytes(data).addOnSuccessListener {
            val bm: Bitmap = BitmapFactory.decodeByteArray(it,0,it.size)
            image_view.setImageBitmap(bm)

        }
        .addOnFailureListener {
            image_view.setImageResource(R.drawable.quiz)
        }


    }

    private class ListRowHolder(row: View?) {
        val question: TextView = row!!.findViewById<TextView>(R.id.question) as TextView
        val response: TextView = row!!.findViewById<TextView>(R.id.response) as TextView
        var id: String = ""
        val username: TextView = row!!.findViewById<TextView>(R.id.username) as TextView
        val image: ImageView = row!!.findViewById<ImageView>(R.id.userImage) as ImageView

    }
}
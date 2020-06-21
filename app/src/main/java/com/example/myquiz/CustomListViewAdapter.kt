package com.example.myquiz

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.myquiz.ui.AskQuestions.AskQuestionsFragment

class CustomListViewAdapter(context: Context, CommentList: MutableList<Comment>) : BaseAdapter() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var itemList = CommentList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val objectId: String = itemList.get(position).objectId as String
        val itemText: String = itemList.get(position).itemText as String
        val response: String = itemList.get(position).response as String

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

    private class ListRowHolder(row: View?) {
        val question: TextView = row!!.findViewById<TextView>(R.id.question) as TextView
        val response: TextView = row!!.findViewById<TextView>(R.id.response) as TextView
        var id: String = ""
    }
}
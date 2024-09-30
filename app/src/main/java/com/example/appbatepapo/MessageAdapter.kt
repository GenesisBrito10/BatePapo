package com.example.appbatepapo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

data class Message(val senderId: String, val text: String)

class MessageAdapter(
    view: Context,
    private val messages: List<Message>

) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(view)
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun getCount(): Int = messages.size

    override fun getItem(position: Int): Any = messages[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val message = messages[position]

        // Determinar o layout com base no remetente
        val layoutId = if (message.senderId == userId) {
            R.layout.message_right
        } else {
            R.layout.message_left
        }


        val view: View
        if (convertView == null || convertView.tag != layoutId) {
            view = inflater.inflate(layoutId, parent, false)
            view.tag = layoutId
        } else {
            view = convertView
        }

        val messageText = view.findViewById<TextView>(R.id.message)
        messageText.text = message.text

        return view
    }

}

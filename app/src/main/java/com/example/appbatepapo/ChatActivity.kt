package com.example.appbatepapo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ChatActivity : AppCompatActivity() {

    private lateinit var listViewMessages: ListView
    private lateinit var textMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var messagesList: ArrayList<Message>
    private lateinit var adapter: MessageAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var barNome: TextView

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private var chatId: String = ""
    private var contactId: String = ""
    private var contactName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        listViewMessages = findViewById(R.id.listViewMessages)
        textMessage = findViewById(R.id.textMessage)
        btnSend = findViewById(R.id.btnSend)
        barNome = findViewById(R.id.contactName)

        db = FirebaseFirestore.getInstance()
        messagesList = ArrayList()
        adapter = MessageAdapter(this,messagesList)
        listViewMessages.adapter = adapter

        contactId = intent.getStringExtra("contactId") ?: ""
        contactName = intent.getStringExtra("contactName") ?: ""
        barNome.text = contactName
        chatId = pegarChat(currentUser!!.uid, contactId)


        btnSend.setOnClickListener {
            enviarMensagem()
        }

        ouvirMensagens()
    }

    private fun pegarChat(uid1: String, uid2: String): String {
        return if (uid1 < uid2) uid1 + uid2 else uid2 + uid1
    }

    private fun enviarMensagem() {
        val texto = textMessage.text.toString()
        if (texto.isNotEmpty()) {

            val mensagem = hashMapOf(
                "senderId" to currentUser!!.uid,
                "text" to texto,
                "timestamp" to FieldValue.serverTimestamp(),
                "read" to false
            )
            db.collection("chats").document(chatId).collection("messages")
                .add(mensagem)
            textMessage.text.clear()
        }
    }

    private fun ouvirMensagens() {
        db.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                for (doc in snapshots!!) {
                    val senderId = doc.getString("senderId") ?: ""
                    val text = doc.getString("text") ?: ""
                    messagesList.add(Message(senderId,text))

                }
                adapter.notifyDataSetChanged()
                listViewMessages.post { listViewMessages.setSelection(adapter.count - 1) }

            }
    }




}
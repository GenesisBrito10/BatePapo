
package com.example.appbatepapo

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeActivity : AppCompatActivity() {

    private lateinit var listViewUsers: ListView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var namesList: ArrayList<String>
    private lateinit var db: FirebaseFirestore

    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()

        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        setContentView(R.layout.activity_home)
        listViewUsers = findViewById(R.id.listViewUsers)
        userList = ArrayList()
        namesList = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, namesList)
        listViewUsers.adapter = adapter

        listViewUsers.setOnItemClickListener { parent, view, position, id ->
            val contact = userList[position]
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("contactName", contact.nome)
            intent.putExtra("contactId", contact.uid)
            startActivity(intent)
        }
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }


        carregarUsuarios()


    }



    private fun carregarUsuarios() {
        db.collection("users")
            .orderBy("nome", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                    userList.clear()
                    namesList.clear()
                    for (document in snapshots!!) {
                        val uid = document.getString("uid") ?: ""
                        val nome = document.getString("nome") ?: ""
                        val email = document.getString("email") ?: ""

                        if (uid != currentUser?.uid) {
                            val user = User(uid, nome, email)
                            userList.add(user)
                            namesList.add(nome)
                        }
                    }
                    adapter.notifyDataSetChanged()

            }
    }






}
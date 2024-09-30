package com.example.appbatepapo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var fb: FirebaseAuth
    private lateinit var nome: EditText
    private lateinit var email: EditText
    private lateinit var senha: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var btnLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        fb = FirebaseAuth.getInstance()
        nome = findViewById(R.id.name)
        email = findViewById(R.id.email)
        senha = findViewById(R.id.senha)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnLogin = findViewById(R.id.btnLogin)

        btnRegistrar.setOnClickListener {
            registrarUsuario()
        }

        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }


    private fun registrarUsuario() {
        val email = email.text.toString().trim()
        val nome = nome.text.toString().trim()
        val senha = senha.text.toString().trim()


        if (email.isEmpty()) {
            this.email.error = "Email é obrigatório"
            this.email.requestFocus()
            return
        }

        if (senha.isEmpty()) {
            this.senha.error = "Senha é obrigatória"
            this.senha.requestFocus()
            return
        }

        fb.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    salvarUsuarioNoFirestore(nome)
                } else {

                    Toast.makeText(this, "Erro ao registrar !", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun salvarUsuarioNoFirestore(nome: String) {
        val db = FirebaseFirestore.getInstance()
        val user = hashMapOf(
            "uid" to fb.currentUser?.uid,
            "nome" to nome,
            "email" to fb.currentUser?.email
        )


        db.collection("users").document(fb.currentUser!!.uid)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Registrado com sucesso!", Toast.LENGTH_SHORT).show()
                home()
            }
            .addOnFailureListener {

                Toast.makeText(this, "Erro ao salvar os dados !", Toast.LENGTH_SHORT).show()
            }
    }


    private fun home() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}


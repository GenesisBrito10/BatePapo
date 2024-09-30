package com.example.appbatepapo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var fb: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var senha: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegistro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        fb = FirebaseAuth.getInstance()
        email = findViewById(R.id.email)
        senha = findViewById(R.id.senha)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegistro = findViewById(R.id.registro)



        if (fb.currentUser != null) {
            navegarParaListaUsuarios()
        }

        btnLogin.setOnClickListener {
            loginUsuario()
        }

        btnRegistro.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUsuario() {
        val email = email.text.toString()
        val senha = senha.text.toString()

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        fb.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navegarParaListaUsuarios()
                } else {
                    Toast.makeText(this, "Erro ao fazer login", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navegarParaListaUsuarios() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}

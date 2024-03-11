package com.example.cryptoview.fragment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoview.MainActivity
import com.example.cryptoview.R

class LoginFragment:AppCompatActivity() {
    private lateinit var userEdt: EditText
    private lateinit var passEdt: EditText
    private lateinit var loginBtn: Button

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.fragment_login)
            initView()
            setVariable()
        }

        private fun initView() {
            userEdt = findViewById(R.id.editTextTextPersonName)
            passEdt = findViewById(R.id.editTextTextPassword)
            loginBtn = findViewById(R.id.loginBtn)
        }

        private fun setVariable() {
            loginBtn.setOnClickListener {
                if (userEdt.text.toString().isEmpty() || passEdt.text.toString().isEmpty()) {
                    Toast.makeText(this@LoginFragment, "Please fill the login form", Toast.LENGTH_SHORT).show()
                } else if (userEdt.text.toString() == "admin" && passEdt.text.toString() == "admin") {
                    startActivity(Intent(this@LoginFragment, MainActivity::class.java))
                }
            }

    }
}
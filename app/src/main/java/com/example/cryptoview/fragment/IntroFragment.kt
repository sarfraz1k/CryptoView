package com.example.cryptoview.fragment

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoview.R

class IntroFragment : AppCompatActivity() {
    private lateinit var goBtn: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_intro)
        initView()
        setVariable()
    }

    private fun initView() {
        goBtn=findViewById(R.id.goBtn)

    }


    private fun setVariable() {
      goBtn.setOnClickListener{
          startActivity(Intent(this@IntroFragment, LoginFragment::class.java))
      }
    }




}
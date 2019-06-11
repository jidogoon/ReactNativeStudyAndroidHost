package com.jidogoon.rnhostandroidapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnReactNative.setOnClickListener {
            startRNActivity()
        }

        startRNActivity()
    }

    private fun startRNActivity() {
        Intent(this, RNAppActivity::class.java).run {
            startActivity(this)
        }
    }
}

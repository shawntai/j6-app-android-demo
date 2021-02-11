package com.siemens.j6_app_android_demo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_page)
    }

    fun actualOnCreate(view: View) {
        setContentView(R.layout.sign_up_log_in_page)
    }

    fun switchMode(view: View) {
        val selected: TextView = findViewById(R.id.selected)
        val notSelected: TextView = findViewById(R.id.not_selected)
        val forgotPassword: TextView = findViewById(R.id.forgot_password)
        selected.text = notSelected.text.also { notSelected.text = selected.text }
        forgotPassword.visibility = View.INVISIBLE + View.VISIBLE - forgotPassword.visibility
    }

    fun toHomePage(view: View) {
        startActivity(Intent(this, HomePageActivity::class.java))
    }
}
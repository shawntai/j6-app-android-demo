package com.siemens.j6_app_android_demo.activities.home_page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.siemens.j6_app_android_demo.R

class InterruptDescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interrupt_description)

        findViewById<TextView>(R.id.save_btn).setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("interruption_description", findViewById<EditText>(R.id.input).text.toString())
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        findViewById<ImageView>(R.id.back).setOnClickListener {
            val resultIntent = Intent()
            val returned: String? = null
            resultIntent.putExtra("interruption_description", returned)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
package com.siemens.j6_app_android_demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        /****** Create Thread that will sleep for 5 seconds */
        val background: Thread = object : Thread() {
            override fun run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(3 * 1000.toLong())

                    // After 5 seconds redirect to another intent
                    val i = Intent(baseContext, LogInActivity::class.java)
                    startActivity(i)

                    //Remove activity
                    finish()
                } catch (e: Exception) {
                }
            }
        }
        // start thread
        background.start()
    }
}
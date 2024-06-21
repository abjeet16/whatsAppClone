package com.example.whatsappclone.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.example.whatsappclone.MainActivity
import com.example.whatsappclone.R
import com.google.firebase.auth.FirebaseAuth

class splashScreen : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        firebaseAuth = FirebaseAuth.getInstance()

        //turn off night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Handler(Looper.getMainLooper()).postDelayed({
            if (firebaseAuth.currentUser!=null)
                startActivity(Intent(this, MainActivity::class.java))
            else
                startActivity(Intent(this,phoneNumberActivity::class.java));
            finish();
        },2000)
    }
}
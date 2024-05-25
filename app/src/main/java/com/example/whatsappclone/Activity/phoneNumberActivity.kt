package com.example.whatsappclone.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.whatsappclone.MainActivity
import com.example.whatsappclone.R
import com.example.whatsappclone.databinding.ActivityPhoneNumberBinding
import com.google.firebase.auth.FirebaseAuth

class phoneNumberActivity : AppCompatActivity() {

    private val binding:ActivityPhoneNumberBinding by lazy {
        ActivityPhoneNumberBinding.inflate(layoutInflater)
    }
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser!= null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        binding.sendOTP.setOnClickListener {
            if(binding.loginCountrycode.isValidFullNumber()){
                binding.PhoneNumber.setError("Phone number not valid");
                return@setOnClickListener
            }
            intent =Intent(this,OTPActivity::class.java);
            intent.putExtra("phoneNumber",binding.loginCountrycode.getFullNumberWithPlus());
            startActivity(intent);
        }
    }
}
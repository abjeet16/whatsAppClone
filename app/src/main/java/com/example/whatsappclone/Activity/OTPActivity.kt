package com.example.whatsappclone.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsappclone.MainActivity
import com.example.whatsappclone.databinding.ActivityOtpactivityBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {
    private val binding: ActivityOtpactivityBinding by lazy {
        ActivityOtpactivityBinding.inflate(layoutInflater)
    }
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var verificationID: String
    private lateinit var dialog: AlertDialog
    private lateinit var phoneNumber:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val builder = AlertDialog.Builder(this)
        builder.setMessage("OTP is on its way")
        builder.setTitle("Loading")
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()

        phoneNumber = intent.getStringExtra("phoneNumber").toString()
        Log.d("OTPActivity", "Phone number: $phoneNumber")
        VerifyNumber(phoneNumber)

        binding.loginNextBtn.setOnClickListener {
            verifyOTP(binding.loginOtp.text.toString())
        }
    }

    private fun verifyOTP(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationID, code)
        SignInByCredentials(credential)
    }

    private fun SignInByCredentials(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                dialog.dismiss()
                if (task.isSuccessful) {
                    Toast.makeText(this, "done", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,ProfileActivity::class.java)
                    intent.putExtra("number",phoneNumber)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("OTPActivity", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun VerifyNumber(phoneNumber: String) {
        val option = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(option)
    }

    private val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            val code = phoneAuthCredential.smsCode
            if (code != null) {
                verifyOTP(code)
            } else {
                // If the code is null, sign in with the credential directly
                SignInByCredentials(phoneAuthCredential)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            dialog.dismiss()
            Log.e("OTPActivity", "onVerificationFailed", e)
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    Toast.makeText(this@OTPActivity, "Invalid request", Toast.LENGTH_SHORT).show()
                }
                is FirebaseTooManyRequestsException -> {
                    Toast.makeText(this@OTPActivity, "SMS quota exceeded", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this@OTPActivity, "Check your mobile number", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId, token)
            verificationID = verificationId
            dialog.dismiss()
            Toast.makeText(this@OTPActivity, "Code sent", Toast.LENGTH_SHORT).show()
        }
    }
}


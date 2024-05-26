package com.example.whatsappclone

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.whatsappclone.Activity.phoneNumberActivity
import com.example.whatsappclone.Utils.FireBaseUtils
import com.example.whatsappclone.adapters.ViewPagerAdapter
import com.example.whatsappclone.databinding.ActivityMainBinding
import com.example.whatsappclone.fragments.callFragment
import com.example.whatsappclone.fragments.chatfragment
import com.example.whatsappclone.fragments.statusFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val fragmentArrayList = ArrayList<Fragment>()
        fragmentArrayList.add(chatfragment())
        fragmentArrayList.add(statusFragment())
        fragmentArrayList.add(callFragment())

        val adapter = ViewPagerAdapter(this,supportFragmentManager,fragmentArrayList)

        binding.ViewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.ViewPager)
        auth = FirebaseAuth.getInstance()

        binding.LogOut.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }
    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Logout")
        builder.setMessage("Are you sure you want to logout?")

        builder.setPositiveButton("Yes") { dialog, which ->
            // User clicked Yes button
            auth.signOut()
            // Redirect to login screen
            val intent = Intent(this,phoneNumberActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        builder.setNegativeButton("No") { dialog, which ->
            // User clicked No button
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}
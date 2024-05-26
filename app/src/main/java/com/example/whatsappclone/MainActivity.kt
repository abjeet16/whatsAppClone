package com.example.whatsappclone

import android.content.Intent
import android.os.Bundle
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

        binding.LogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,phoneNumberActivity::class.java))
        }
    }
}
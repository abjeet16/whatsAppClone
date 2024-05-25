package com.example.whatsappclone

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.whatsappclone.adapters.ViewPagerAdapter
import com.example.whatsappclone.databinding.ActivityMainBinding
import com.example.whatsappclone.fragments.callFragment
import com.example.whatsappclone.fragments.chatfragment
import com.example.whatsappclone.fragments.statusFragment

class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val fragmentArrayList = ArrayList<Fragment>()
        fragmentArrayList.add(callFragment())
        fragmentArrayList.add(chatfragment())
        fragmentArrayList.add(statusFragment())

        val adapter = ViewPagerAdapter(this,supportFragmentManager,fragmentArrayList)

        binding.ViewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.ViewPager)
    }
}
package com.example.whatsappclone.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.whatsappclone.R
import com.example.whatsappclone.databinding.ActivityStatusViewBinding
import com.example.whatsappclone.datamodels.Status

class StatusView : AppCompatActivity() {
    private val binding: ActivityStatusViewBinding by lazy {
        ActivityStatusViewBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val StatusList = intent.getSerializableExtra("statusList") as? ArrayList<Status>
        Glide.with(this).load(StatusList?.get(0)?.imageUrl).into(binding.Image)
        binding.Caption.text = StatusList?.get(0)?.statusText
    }
}
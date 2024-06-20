package com.example.whatsappclone.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.whatsappclone.R
import com.example.whatsappclone.databinding.ActivityOthersStatusViewBinding
import com.example.whatsappclone.databinding.ActivityStatusViewBinding
import com.example.whatsappclone.databinding.OthersStatusLayoutBinding
import com.example.whatsappclone.datamodels.Status

class OthersStatusVIew : AppCompatActivity() {
    private val binding: ActivityOthersStatusViewBinding by lazy {
        ActivityOthersStatusViewBinding.inflate(layoutInflater)
    }
    private var statusNumber: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val StatusList = intent.getCharSequenceArrayListExtra("statusList")
        Glide.with(this).load(StatusList?.get(statusNumber)?.imageUrl).into(binding.Image)
        binding.Caption.text = StatusList?.get(statusNumber)?.statusText
        binding.storyBack.isEnabled = false
        if (StatusList != null) {
            if (StatusList.size == 1)
                binding.storyNext.isEnabled = false
        }
        setUpClickListeners(StatusList)
    }

    private fun setUpClickListeners(StatusList: ArrayList<Status>?) {
        binding.apply {
            cancelButton.setOnClickListener {
                finish()
            }
            storyBack.setOnClickListener {
                storyNext.isEnabled = true
                statusNumber--
                if (statusNumber==0){
                    storyBack.isEnabled = false
                }
                Glide.with(this@OthersStatusVIew).load(StatusList?.get(statusNumber)?.imageUrl).into(Image)
                Caption.text = StatusList?.get(statusNumber)?.statusText
            }
            storyNext.setOnClickListener {
                storyBack.isEnabled = true
                statusNumber++
                if (statusNumber==StatusList?.size?.minus(1)) {
                    storyNext.isEnabled = false
                }
                Glide.with(this@OthersStatusVIew).load(StatusList?.get(statusNumber)?.imageUrl).into(Image)
                Caption.text = StatusList?.get(statusNumber)?.statusText
            }
        }
    }
}
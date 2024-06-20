package com.example.whatsappclone.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.whatsappclone.databinding.ActivityOthersStatusViewBinding
import java.util.ArrayList

class OthersStatusVIew : AppCompatActivity() {
    private val binding: ActivityOthersStatusViewBinding by lazy {
        ActivityOthersStatusViewBinding.inflate(layoutInflater)
    }
    private var statusNumber: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val StatusImageList = intent.getStringArrayListExtra("statusImageList")
        val StatusTextList = intent.getStringArrayListExtra("statusTextList")
        Glide.with(this).load(StatusImageList?.get(statusNumber)).into(binding.Image)
        binding.Caption.text = StatusTextList?.get(statusNumber)
        binding.storyBack.isEnabled = false
        if (StatusImageList != null) {
            if (StatusImageList.size == 1)
                binding.storyNext.isEnabled = false
        }
        setUpClickListeners(StatusImageList)
    }

    private fun setUpClickListeners(StatusList: ArrayList<String>?) {
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
                Glide.with(this@OthersStatusVIew).load(StatusList?.get(statusNumber)).into(Image)
                //Caption.text = StatusList?.get(statusNumber)?.statusText
            }
            storyNext.setOnClickListener {
                storyBack.isEnabled = true
                statusNumber++
                if (statusNumber==StatusList?.size?.minus(1)) {
                    storyNext.isEnabled = false
                }
                Glide.with(this@OthersStatusVIew).load(StatusList?.get(statusNumber)).into(Image)
                Caption.text = StatusList?.get(statusNumber)?.statusText
            }
        }
    }
}
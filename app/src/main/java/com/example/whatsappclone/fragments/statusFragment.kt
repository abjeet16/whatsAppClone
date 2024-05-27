package com.example.whatsappclone.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.whatsappclone.Activity.StatusUploadScreen
import com.example.whatsappclone.databinding.FragmentStatusBinding

class statusFragment : Fragment() {

    private lateinit var binding: FragmentStatusBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatusBinding.inflate(layoutInflater)

        binding.addStatus.setOnClickListener{
            val intent = Intent(context, StatusUploadScreen::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}
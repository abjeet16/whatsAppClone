package com.example.whatsappclone.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsappclone.Activity.chatRoom
import com.example.whatsappclone.adapters.alluserAdapter
import com.example.whatsappclone.databinding.FragmentChatfragmentBinding
import com.example.whatsappclone.datamodels.userDetails
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class chatfragment : Fragment() {

    private lateinit var binding: FragmentChatfragmentBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatfragmentBinding.inflate(layoutInflater)

        firebaseFirestore = Firebase.firestore
        getAllUsers()
        return binding.root
    }

    private fun getAllUsers() {
        firebaseFirestore.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val userList = mutableListOf<userDetails>()
                for (document in result) {
                    val user = document.toObject(userDetails::class.java)
                    if (user.uid != FirebaseAuth.getInstance().uid) {
                        userList.add(user)
                    }
                }
                val adapter = alluserAdapter(userList,requireContext(),::chatItemClicked)
                binding.recyclerViewChat.adapter = adapter
                binding.recyclerViewChat.layoutManager = LinearLayoutManager(requireContext())
            }.addOnFailureListener{
                Toast.makeText(requireContext(),"failed to get all user", Toast.LENGTH_SHORT).show()
            }
    }
    fun chatItemClicked(userDetail:userDetails){
        val intent = Intent(requireContext(),chatRoom::class.java)
        intent.putExtra("userName",userDetail.name)
        intent.putExtra("uid",userDetail.uid)
        intent.putExtra("profileImageUrl",userDetail.profileImageUrl)
        startActivity(intent)
    }
}
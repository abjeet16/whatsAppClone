package com.example.whatsappclone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.databinding.ItemMessageBinding
import com.example.whatsappclone.datamodels.ChatMessage
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(private val messages: List<ChatMessage>): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            if (messages[position].senderId == FirebaseAuth.getInstance().currentUser?.uid){
                binding.textMessageSent.visibility = View.VISIBLE
                binding.textMessageReceived.visibility = View.GONE
                binding.textMessageSent.text = messages[position].message
            }else{
                binding.textMessageSent.visibility = View.GONE
                binding.textMessageReceived.visibility = View.VISIBLE
                binding.textMessageReceived.text = messages[position].message
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }
}
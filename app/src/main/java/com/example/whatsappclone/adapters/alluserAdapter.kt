package com.example.whatsappclone.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsappclone.databinding.ItemUserBinding
import com.example.whatsappclone.datamodels.userDetails

class alluserAdapter(
    private val listOFalluser: List<userDetails>,
    private val context: Context,
    val chatItemClicked:(userDetails)-> Unit
): RecyclerView.Adapter<alluserAdapter.alluserViewHolder>() {
    inner class alluserViewHolder(val binding:ItemUserBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val uri = Uri.parse(listOFalluser[position].profileImageUrl)
            binding.textViewName.text = listOFalluser[position].name
            binding.recentText.text = listOFalluser[position].phoneNumber
            Glide.with(context).load(uri).into(binding.imageViewProfile)
            binding.root.setOnClickListener {
                chatItemClicked(listOFalluser[position])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): alluserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return alluserViewHolder(binding)
    }

    override fun getItemCount(): Int = listOFalluser.size

    override fun onBindViewHolder(holder: alluserViewHolder, position: Int) {
        holder.bind(position)
    }
}
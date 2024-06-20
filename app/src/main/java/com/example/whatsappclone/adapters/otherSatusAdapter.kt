package com.example.whatsappclone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.databinding.OthersStatusLayoutBinding
import com.example.whatsappclone.datamodels.othersStatus

class otherSatusAdapter(private val statusList: List<othersStatus>) :RecyclerView.Adapter<otherSatusAdapter.ViewHolder>() {
    inner class ViewHolder(val binding:OthersStatusLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            //binding.personName.text =
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OthersStatusLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = statusList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }
}
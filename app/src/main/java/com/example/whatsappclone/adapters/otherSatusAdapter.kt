package com.example.whatsappclone.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsappclone.databinding.OthersStatusLayoutBinding
import com.example.whatsappclone.datamodels.Status
import com.example.whatsappclone.datamodels.othersStatus
import com.example.whatsappclone.datamodels.userDetails
import java.util.concurrent.TimeUnit

class otherSatusAdapter(private val statusList: List<othersStatus>,
                        private val context: Context,
                        val chatItemClicked:(Array<Status>)-> Unit)
    :RecyclerView.Adapter<otherSatusAdapter.ViewHolder>() {
    inner class ViewHolder(val binding:OthersStatusLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val uri = Uri.parse(statusList[position].status[0].imageUrl)
            val time = statusList[position].status[0].timestamp
            binding.personName.text = statusList[position].status[0].personName
            Glide.with(context).load(uri).into(binding.statusImage)
            binding.statusUpdateTime.text = convertMillisToHoursAndMinutes(System.currentTimeMillis()-time!!)

            binding.root.setOnClickListener {
                chatItemClicked(statusList[position].status.toTypedArray())
            }
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
    private fun convertMillisToHoursAndMinutes(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(hours)
        if (hours>24){
            val days = TimeUnit.MILLISECONDS.toDays(millis)
            return String.format("%d days ago", days)
        }else if (hours.toInt() == 0){
            return String.format("%d min ago",minutes)
        }else if (minutes.toInt() == 0){
            return String.format("just now")
        }else if (hours.toInt() >= 1){
            return String.format("%d hr ago",hours)
        }
        return String.format("%d hr %d min ago",hours,minutes)
    }
}
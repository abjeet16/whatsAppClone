package com.example.whatsappclone.fragments

import android.content.Intent
import android.os.Bundle
import java.util.concurrent.TimeUnit
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.whatsappclone.Activity.StatusUploadScreen
import com.example.whatsappclone.Activity.StatusView
import com.example.whatsappclone.Utils.FireBaseUtils
import com.example.whatsappclone.adapters.alluserAdapter
import com.example.whatsappclone.databinding.FragmentStatusBinding
import com.example.whatsappclone.datamodels.Status
import com.example.whatsappclone.datamodels.userDetails
import com.google.firebase.auth.FirebaseAuth

class statusFragment : Fragment() {

    private lateinit var binding: FragmentStatusBinding
    private val fireBaseUtils = FireBaseUtils()
    private var myStatusList:MutableList<Status> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatusBinding.inflate(layoutInflater)

        setMyStatus()
        setUpClickListener()
        setUpOthersStatus()
        return binding.root
    }

    private fun setUpOthersStatus() {
        fireBaseUtils.getAllTheUsersWithStatus()
            .get()
            .addOnSuccessListener {
                result->
                if (result.isEmpty){
                    Toast.makeText(requireContext(),"no user found", Toast.LENGTH_SHORT).show()
                }else{
                    val allUsers = mutableListOf<userDetails>()
                    for (document in result){
                        val userID = document.toObject(userDetails::class.java)
                        allUsers.add(userID)
                    }
                    for (i in allUsers){
                        fireBaseUtils.getOthersStatusReference(i.uid.toString())
                            .get()
                            .addOnSuccessListener {
                                result->
                                val othersStatus = mutableListOf<Status>()
                                for (document in result){
                                    val userID = document.toObject(Status::class.java)
                                    othersStatus.add(userID)
                                }
                                for (i in othersStatus)
                                Log.d("bhhvda",i.statusText.toString())
                            }
                    }
                }
            }
    }

    private fun setUpClickListener() {
        binding.addStatus.setOnClickListener{
            val intent = Intent(context, StatusUploadScreen::class.java)
            startActivity(intent)
        }
        binding.StatusCard.setOnClickListener{
            if (myStatusList.isNotEmpty()) {
                val intent = Intent(requireContext(), StatusView::class.java)
                intent.putExtra("statusList", ArrayList(myStatusList))
                startActivity(intent)
            }
        }
    }

    private fun setMyStatus() {
        fireBaseUtils.getStatusReference()
            .get()
            .addOnSuccessListener { result ->
                if (result.documents.isNotEmpty()) {
                    myStatusList = mutableListOf()
                    for (document in result) {
                        val status = document.toObject(Status::class.java)
                        myStatusList.add(status)
                    }
                    myStatusList = myStatusList.sortedByDescending { it.timestamp }.toMutableList()
                    Glide.with(requireContext()).load(myStatusList[0].imageUrl).into(binding.StatusImage)
                    binding.timeAgo.text = convertMillisToHoursAndMinutes(System.currentTimeMillis()-myStatusList[0].timestamp!!)
                }
            }.addOnFailureListener{
                Toast.makeText(requireContext(),"failed to get all user", Toast.LENGTH_SHORT).show()
            }
    }
    private fun convertMillisToHoursAndMinutes(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(hours)
        if (hours>24){
            val days = TimeUnit.MILLISECONDS.toDays(millis)
            return String.format("%d days ago", days)
        }
        return String.format("%02dhr:%02dmin ago", hours, minutes)
    }
}
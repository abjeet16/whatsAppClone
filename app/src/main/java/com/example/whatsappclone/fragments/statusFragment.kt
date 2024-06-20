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
import com.bumptech.glide.Glide
import com.example.whatsappclone.Activity.StatusUploadScreen
import com.example.whatsappclone.Activity.StatusView
import com.example.whatsappclone.Utils.FireBaseUtils
import com.example.whatsappclone.databinding.FragmentStatusBinding
import com.example.whatsappclone.datamodels.Status
import com.example.whatsappclone.datamodels.othersStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
        //fireBaseUtils.getAllTheUsersWithStatus()
            FirebaseFirestore.getInstance().collection("Status")
            .get()
            .addOnSuccessListener {
                result->
                if (result.isEmpty){
                    Toast.makeText(requireContext(),"no user found", Toast.LENGTH_SHORT).show()
                }else{
                    val documentNames = mutableListOf<String>()
                    for (document in result) {
                        //this adds a filter to remove the current user
                        if (document.id!=FirebaseAuth.getInstance().currentUser?.uid)
                        documentNames.add(document.id)
                    }
                    getOthersStatus(documentNames)
                }
            }
    }

    private fun getOthersStatus(documentNames: MutableList<String>) {
        val singlePersonStatusList = mutableListOf<Status>()
        val statusListToPass = mutableListOf<othersStatus>()
        for (i in documentNames) {
            FirebaseFirestore.getInstance().collection("Status").document(i).collection("status")
                .get()
                .addOnSuccessListener { result ->
                    singlePersonStatusList.clear()
                    for (document in result) {
                        val user = document.toObject(Status::class.java)
                        singlePersonStatusList.add(user)
                    }
                    statusListToPass.add(othersStatus(singlePersonStatusList))

                    //if (statusListToPass.size==2)
                        //Log.d(statusListToPass[1].status[0].statusText, "huhvdhcavd")
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "failed to get all user", Toast.LENGTH_SHORT)
                        .show()
                }
        }
        for (x in statusListToPass){
            for (y in x.status){
                Log.d(y.statusText, "huhvdhcavd")
            }
            Log.d("break", "huhvdhcavd")
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
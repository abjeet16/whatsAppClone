package com.example.whatsappclone.Activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.whatsappclone.Utils.FireBaseUtils
import com.example.whatsappclone.adapters.ChatAdapter
import com.example.whatsappclone.databinding.ActivityChatRoomBinding
import com.example.whatsappclone.datamodels.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log

class chatRoom : AppCompatActivity() {
    private val binding: ActivityChatRoomBinding by lazy {
        ActivityChatRoomBinding.inflate(layoutInflater)
    }

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    private lateinit var OtherUSerID:String
    private val fireBaseUtils = FireBaseUtils()
    private lateinit var RoomId:String
    private lateinit var adapter: ChatAdapter
    private lateinit var currentUserId:String
    private lateinit var firestore: FirebaseFirestore
    private lateinit var ALlMessage:MutableList<ChatMessage>
    private var lastMessageTime:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setProfileAndUserName()
        RoomId = getChatRoomId()
        firestore = FirebaseFirestore.getInstance()

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                // Call function to update the UI
                setUpRecyclerView()

                // Re-post the runnable with a delay of 1 second
                handler.postDelayed(this, 1000)
            }
        }
        // Start the repeating task
        handler.post(runnable)

        binding.sendButton.setOnClickListener {
            sendMessage()
        }
    }

    private fun setUpRecyclerView() {
        val chatRef = fireBaseUtils.getAllChatReference(RoomId)
        if (chatRef!=null) {
            chatRef.get()
                .addOnSuccessListener { result ->
                    //checking if the data is empty or not OR
                    //if the DataReference exists or NOT
                    if (result!=null && result.size()>0) {
                        val allMessage = mutableListOf<ChatMessage>()
                        for (document in result) {
                            val chat = document.toObject(ChatMessage::class.java)
                            allMessage.add(chat)
                        }
                        if (allMessage.isNotEmpty()) {
                            //sorting the chats on  basis of time
                            ALlMessage = allMessage.sortedBy { it.timestamp }
                                    as MutableList<ChatMessage>
                        }
                        if (lastMessageTime != ALlMessage[ALlMessage.size - 1].timestamp && ALlMessage.isNotEmpty()) {
                            adapter = ChatAdapter(ALlMessage)
                            lastMessageTime = ALlMessage[ALlMessage.size - 1].timestamp!!
                            binding.recyclerView.adapter = adapter
                            binding.recyclerView.layoutManager = LinearLayoutManager(this)
                                .apply {
                                    stackFromEnd = true
                                }
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "getting chat failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun sendMessage() {
        val message = binding.messageEditText.text.toString()
        if (message.isEmpty()){
            return
        }
        //clear current chat in Edit Text
        binding.messageEditText.text.clear()

        // Scroll to the bottom when the current user sends a message
        //binding.recyclerView.scrollToPosition(adapter.itemCount - 1)

        val messageUserModel = ChatMessage(
            senderId = currentUserId,
            message = message,
            MessageSeen = null,
            timestamp = System.currentTimeMillis()
        )
        firestore.collection("ChatRooms").document(RoomId).collection("chats")
            .add(messageUserModel)
            .addOnSuccessListener {
                Toast.makeText(this, "message send", Toast.LENGTH_SHORT).show()
                setUpRecyclerView()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getChatRoomId():String {
        OtherUSerID = intent.getStringExtra("uid")!!
        currentUserId = fireBaseUtils.getCurrentUserId().toString()
        Log.d("jbcbajbcjbab", fireBaseUtils.getChatroomId(currentUserId,OtherUSerID))
        return fireBaseUtils.getChatroomId(currentUserId,OtherUSerID)
    }

    private fun setProfileAndUserName() {
        Glide.with(this).load(intent.getStringExtra("profileImageUrl")).into(binding.profileImageView)
        binding.profileNameTextView.text = intent.getStringExtra("userName")
    }
    override fun onDestroy() {
        super.onDestroy()
        // Remove the callbacks to stop the repeating task when the activity is destroyed
        handler.removeCallbacks(runnable)
    }
}
package com.example.whatsappclone.Utils

import android.widget.Toast
import com.example.whatsappclone.Activity.StatusUploadScreen
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class FireBaseUtils {
    fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }
    fun getStatusReference(): CollectionReference {
        return FirebaseFirestore.getInstance()
            .collection("Status")
            .document(getCurrentUserId()!!)
            .collection("status")
    }
    fun getOthersStatusReference(userId: String): CollectionReference {
        return FirebaseFirestore.getInstance()
            .collection("Status")
            .document(userId)
            .collection("status")
    }
    fun getUserName(): DocumentReference {
        return FirebaseFirestore.getInstance()
            .collection("users")
            .document(getCurrentUserId()!!)
    }
    fun setUserIdInSideStatus(statusUploadScreen: StatusUploadScreen) {
        val userId = getCurrentUserId()
        if (userId != null) {
            FirebaseFirestore.getInstance()
                .collection("Status")
                .document(userId)
                .set(mapOf("uid" to userId)) // Assuming you want to set the userId as the content
                .addOnSuccessListener {
                    Toast.makeText(statusUploadScreen, "Success", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(statusUploadScreen, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(statusUploadScreen, "User ID is null", Toast.LENGTH_SHORT).show()
        }
    }
    fun getAllTheUsersWithStatus(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("Status")
    }
    fun getChatroomId(userId1: String, userId2: String): String {
        return if (userId1.hashCode() < userId2.hashCode()) {
            userId1 + "_" + userId2
        } else {
            userId2 + "_" + userId1
        }
    }
    fun getAllChatReference(chatroomId: String): CollectionReference? {
        try{
            return Firebase.firestore.collection("ChatRooms").document(chatroomId)
                .collection("chats")
        }catch(e: Exception){
            return null
        }
    }
}
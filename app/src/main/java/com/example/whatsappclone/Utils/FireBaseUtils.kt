package com.example.whatsappclone.Utils

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class FireBaseUtils {
    fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }
    fun getStatusReference(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("Status").document(getCurrentUserId()!!).collection("status")
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
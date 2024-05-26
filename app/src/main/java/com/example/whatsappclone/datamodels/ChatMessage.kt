package com.example.whatsappclone.datamodels

data class ChatMessage(val imageUrl: String? = null,
                       val message: String? = null,
                       val MessageSeen: Boolean? = null,
                       val senderId: String?= null,
                       val timestamp: Long? = null)

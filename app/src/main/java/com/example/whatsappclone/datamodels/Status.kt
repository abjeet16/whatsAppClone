package com.example.whatsappclone.datamodels

import java.io.Serializable

data class Status(
    var statusId: String? = null,
    var imageUrl: String? = null,
    var statusText: String? = null,
    var timestamp: Long? = null) : Serializable

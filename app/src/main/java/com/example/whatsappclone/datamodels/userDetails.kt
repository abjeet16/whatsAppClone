package com.example.whatsappclone.datamodels

import com.example.whatsappclone.Activity.phoneNumberActivity

data class userDetails(
    var uid: String?= null,
    var name: String?=null,
    var phoneNumber: String?=null,
    var profileImageUrl: String? = null)

package com.example.whatsappclone.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.whatsappclone.MainActivity
import com.example.whatsappclone.R
import com.example.whatsappclone.Utils.FireBaseUtils
import com.example.whatsappclone.databinding.ActivityStatusUploadScreenBinding
import com.example.whatsappclone.datamodels.Status
import com.example.whatsappclone.datamodels.userDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class StatusUploadScreen : AppCompatActivity() {

    private val binding: ActivityStatusUploadScreenBinding by lazy {
        ActivityStatusUploadScreenBinding.inflate(layoutInflater)
    }
    private lateinit var profileImageLocalPath: String
    private lateinit var statusInfo:Status
    private val fireBaseUtils = FireBaseUtils()
    private lateinit var imageURI:String
    private lateinit var statusPhotoUUID:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        pickImage.launch("image/*")

        buttonClickListeners()

    }

    private fun buttonClickListeners() {
        binding.apply {
            selectAnotherImage.setOnClickListener {
                pickImage.launch("image/*")
            }
            cancelButton.setOnClickListener{
                finish()
            }
            uploadImage.setOnClickListener {
                binding.uploadImage.visibility = View.GONE
                uploadImageToFirebase(Uri.parse(profileImageLocalPath))
            }
        }
    }

    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){ uri->
        if (uri != null){
            binding.Image.setImageURI(uri)
            profileImageLocalPath = uri.toString()
        }
    }
    private fun uploadImageToFirebase(fileUri: Uri?) {
        statusPhotoUUID = UUID.randomUUID().toString()
        if (fileUri != null) {
            val storageReference = FirebaseStorage.getInstance().reference
            val fileReference = storageReference.child(
                "Status/${statusPhotoUUID}.${getFileExtension(fileUri)}")

            fileReference.putFile(fileUri)
                .addOnSuccessListener { taskSnapshot ->
                    // Get the download URL
                    taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                        imageURI = uri.toString()
                        uploadStatusInfoToFirebase()
                    }
                }
                .addOnFailureListener { e ->
                    // Handle unsuccessful uploads
                    binding.uploadImage.visibility = View.VISIBLE
                }
                .addOnProgressListener { taskSnapshot -> }
        }
    }

    private fun uploadStatusInfoToFirebase() {
        statusInfo = Status(
            statusId = statusPhotoUUID,
            imageUrl = imageURI,
            statusText = binding.Caption.text.toString(),
            timestamp = System.currentTimeMillis(),
            personName = getUserName()
        )

        fireBaseUtils.getStatusReference()
            .add(statusInfo)
            .addOnSuccessListener {
                fireBaseUtils.setUserIdInSideStatus(this)
                Toast.makeText(this, "status uploaded successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                binding.uploadImage.visibility = View.VISIBLE
                Toast.makeText(this, "Error : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getUserName(callback: FirestoreCallback) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { result ->
                val status = result.toObject(userDetails::class.java)
                val personName = status?.name.toString()
                Toast.makeText(this, personName, Toast.LENGTH_SHORT).show()
                callback.onCallback(personName)
            }.addOnFailureListener { exception ->
                // Handle any errors
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                callback.onCallback("") // Return an empty string on failure
            }
    }



    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }
    interface FirestoreCallback {
        fun onCallback(name: String)
    }

}
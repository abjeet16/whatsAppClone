package com.example.whatsappclone.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsappclone.MainActivity
import com.example.whatsappclone.databinding.ActivityProfileBinding
import com.example.whatsappclone.datamodels.userDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage

class ProfileActivity : AppCompatActivity() {
    private val binding:ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }
    private lateinit var UserDetails:userDetails
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var profilePhoto: String
    private lateinit var profileImageLocalPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        buttonClickableAndProcressBarVisabilty(true)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.loginLetMeInBtn.setOnClickListener {
            buttonClickableAndProcressBarVisabilty(false)
            uploadImageToFirebase(Uri.parse(profileImageLocalPath))
        }

        binding.Profile.setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    private fun getAndsetuserData() {
        // Get the user's phone number from the intent
        // UserDetails.phoneNumber = intent.getStringExtra("phoneNumber").toString()
        UserDetails = userDetails(
            name = binding.loginUsername.text.toString(),
            profileImageUrl = profilePhoto,
            uid = firebaseAuth.uid.toString(),
            phoneNumber = intent.getStringExtra("number").toString()
        )
        Log.d("userDetail", UserDetails.toString())
        saveUserDetails(UserDetails)
    }

    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){ uri->
        if (uri != null){
            binding.Profile.setImageURI(uri)
            profileImageLocalPath = uri.toString()
        }
    }
    private fun saveUserDetails(user: userDetails) {
        firestore.collection("users").document(user.uid!!)
            .set(user)
            .addOnSuccessListener {
                buttonClickableAndProcressBarVisabilty(true)
                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                buttonClickableAndProcressBarVisabilty(true)
                Toast.makeText(this, "Error saving user details: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun uploadImageToFirebase(fileUri: Uri?) {
        if (fileUri != null) {
            val storageReference = FirebaseStorage.getInstance().reference
            val fileReference = storageReference.child("profilePics/${firebaseAuth.uid}.${getFileExtension(fileUri)}")

            fileReference.putFile(fileUri)
                .addOnSuccessListener { taskSnapshot ->
                    // Get the download URL
                    taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        profilePhoto = downloadUrl
                        getAndsetuserData()
                    }
                }
                .addOnFailureListener { e ->
                    // Handle unsuccessful uploads

                }
                .addOnProgressListener { taskSnapshot ->
                    val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                    // Update UI with progress if needed

                }
        }
    }

    private fun buttonClickableAndProcressBarVisabilty(visibility: Boolean){
        if (!visibility){
            binding.loginLetMeInBtn.isEnabled = false
            binding.loginProgressBar.visibility = android.view.View.VISIBLE
        }else {
            binding.loginLetMeInBtn.isEnabled = true
            binding.loginProgressBar.visibility = android.view.View.GONE
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }
}
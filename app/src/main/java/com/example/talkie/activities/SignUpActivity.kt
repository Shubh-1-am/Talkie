package com.example.talkie.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.talkie.databinding.ActivitySignUpBinding
import com.example.talkie.models.User
import com.example.talkie.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private var mAuth: FirebaseAuth? = null
    private var mUser: FirebaseUser? = null
    private var mDatabaseRef: FirebaseDatabase? = null
    private var mStorageRef: FirebaseStorage? = null
    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseUtils.getFirebaseAuth()
        mUser = FirebaseUtils.getFirebaseUser()
        mDatabaseRef = FirebaseDatabase.getInstance()
        mStorageRef = FirebaseStorage.getInstance()

        binding.profileImage.setOnClickListener {
            selectImage()
        }

        binding.signupButton.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            val name = binding.etName.text.toString()

            if (!email.matches(Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"))) {
                binding.etEmail.error = "Invalid Email"
                return@setOnClickListener
            } else if (password.length < 6) {
                binding.etPassword.error = "Password must be at least 6 characters"
                return@setOnClickListener
            } else if (password != confirmPassword) {
                binding.etConfirmPassword.error = "Passwords do not match"
                return@setOnClickListener
            } else if (name.isEmpty()) {
                binding.etName.error = "Name cannot be empty"
                return@setOnClickListener
            } else {
                binding.progressBar.visibility = View.VISIBLE

                mAuth!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            mUser = mAuth!!.currentUser
                            mUser!!.sendEmailVerification()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        binding.progressBar.visibility = View.GONE

                                        if (selectedImageUri != null) {
                                            val storageReference =
                                                mStorageRef!!.reference.child("Profiles")
                                                    .child(mUser!!.uid)
                                            storageReference.putFile(selectedImageUri!!)
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        storageReference.downloadUrl.addOnSuccessListener { uri ->
                                                            val imageUrl = uri.toString()
                                                            val user =
                                                                User(mUser!!.uid, name, imageUrl)

                                                            mDatabaseRef!!.reference.child("users")
                                                                .child(mUser!!.uid)
                                                                .setValue(user)
                                                                .addOnSuccessListener {
                                                                    sendToLogin()
                                                                }
                                                        }
                                                    } else {
                                                        binding.progressBar.visibility = View.GONE
                                                        Toast.makeText(
                                                            this,
                                                            "Failed to upload profile image!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                        } else {
                                            val user = User(mUser!!.uid, name, "No Image")

                                            mDatabaseRef!!.reference.child("users")
                                                .child(mUser!!.uid)
                                                .setValue(user)
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        sendToLogin()
                                                    } else {
                                                        binding.progressBar.visibility = View.GONE
                                                        Toast.makeText(
                                                            this,
                                                            "Failed to create user!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                        }
                                    }
                                }
                        }
                    }
            }

        }


        binding.alreadyHaveAnAccountTv.setOnClickListener {
            sendToLogin()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, RC_SELECT_IMAGE)

    }

    private fun sendToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            Glide.with(this)
                .load(selectedImageUri)
                .into(binding.profileImage)
            binding.addImageIcon.visibility = View.GONE
        }
    }


    companion object {
        private const val RC_SELECT_IMAGE = 1
    }
}
package com.example.talkie.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

object FirebaseUtils {

    private var mAuth: FirebaseAuth? = null
    private var mUser: FirebaseUser? = null
    private var mDatabaseRef: FirebaseDatabase? = null
    private var mStorageRef: FirebaseStorage? = null

    fun getFirebaseAuth(): FirebaseAuth {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance()
        }
        return mAuth!!
    }

    fun getFirebaseUser(): FirebaseUser? {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance()
        }
        if (mUser == null) {
            mUser = mAuth?.currentUser
        }
        return mUser
    }

    fun getDatabase(): FirebaseDatabase {
        if (mDatabaseRef == null) {
            mDatabaseRef = FirebaseDatabase.getInstance()
        }
        return mDatabaseRef!!
    }

    fun getStorage(): FirebaseStorage {
        if (mStorageRef == null) {
            mStorageRef = FirebaseStorage.getInstance()
        }
        return mStorageRef!!
    }
}
package com.example.myapplication.data.repos

import android.net.Uri
import com.example.myapplication.response.ResponseStateResult
import com.example.myapplication.data.database.remot.FirebaseStorageSource

class StorageRepo {
    private val firebaseStorageService = FirebaseStorageSource()

    fun updateUserProfileImage(userID: String, byteArray: ByteArray, b: (ResponseStateResult<Uri>) -> Unit) {
        b.invoke(ResponseStateResult.Loading)
        firebaseStorageService.uploadUserImage(userID, byteArray).addOnSuccessListener {
            b.invoke((ResponseStateResult.Success(it)))
        }.addOnFailureListener {
            b.invoke(ResponseStateResult.Error(it.message))
        }
    }
}
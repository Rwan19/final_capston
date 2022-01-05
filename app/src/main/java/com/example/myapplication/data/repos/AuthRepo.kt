package com.example.myapplication.data.repos

import android.util.Log
import com.example.myapplication.data.database.FirebaseAuthSource
import com.example.myapplication.data.database.FirebaseAuthStateObserver
import com.example.myapplication.ResponseStateResult
import com.example.myapplication.model.CreatUser
import com.example.myapplication.model.Login
import com.google.firebase.auth.FirebaseUser

class AuthRepo{
    private val firebaseAuthService = FirebaseAuthSource()

    fun observeAuthState(stateObserver: FirebaseAuthStateObserver, b: ((ResponseStateResult<FirebaseUser>) -> Unit)){
        firebaseAuthService.attachAuthStateObserver(stateObserver,b)
    }

    fun loginUser(login: Login, b: ((ResponseStateResult<FirebaseUser>) -> Unit)) {
        b.invoke(ResponseStateResult.Loading)
        firebaseAuthService.loginWithEmailAndPassword(login).addOnSuccessListener {
            b.invoke(ResponseStateResult.Success(it.user))
        }.addOnFailureListener {
            b.invoke(ResponseStateResult.Error(msg = it.message))
        }
    }

    fun createUser(createUser:CreatUser, b: ((ResponseStateResult<FirebaseUser>) -> Unit)) {
        b.invoke(ResponseStateResult.Loading)
        firebaseAuthService.createUser(createUser).addOnSuccessListener {
            b.invoke(ResponseStateResult.Success(it.user))

        }.addOnFailureListener {
            b.invoke(ResponseStateResult.Error(msg = it.message))
            Log.d("TAG Create user", "createUser: ${it.message}")
        }
    }

    fun logoutUser() {
        firebaseAuthService.logout()
    }
}
package com.example.myapplication.data.database


import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.myapplication.ResponseStateResult
import com.example.myapplication.model.CreatUser
import com.example.myapplication.model.Login


class FirebaseAuthStateObserver {

    private var authListener: FirebaseAuth.AuthStateListener? = null
    private var instance: FirebaseAuth? = null

    fun start(valueEventListener: FirebaseAuth.AuthStateListener, instance: FirebaseAuth) {
        this.authListener = valueEventListener
        this.instance = instance
        this.instance!!.addAuthStateListener(authListener!!)
    }

    fun clear() {
        authListener?.let { instance?.removeAuthStateListener(it) }
    }
}

class FirebaseAuthSource {

    companion object {
        val authInstance = FirebaseAuth.getInstance()
    }

    private fun attachAuthObserver(b: ((ResponseStateResult<FirebaseUser>) -> Unit)): FirebaseAuth.AuthStateListener {
        return FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                b.invoke(ResponseStateResult.Error("No user"))
            } else { b.invoke(ResponseStateResult.Success(it.currentUser)) }
        }
    }

    fun loginWithEmailAndPassword(login: Login): Task<AuthResult> {
        return authInstance.signInWithEmailAndPassword(login.email, login.password)
    }

    fun createUser(createUser: CreatUser): Task<AuthResult> {
        return authInstance.createUserWithEmailAndPassword(createUser.email, createUser.password)
    }

    fun logout() {
        authInstance.signOut()
    }

    fun attachAuthStateObserver(firebaseAuthStateObserver: FirebaseAuthStateObserver, b: ((ResponseStateResult<FirebaseUser>) -> Unit)) {
        val listener = attachAuthObserver(b)
        firebaseAuthStateObserver.start(listener, authInstance)
    }
}


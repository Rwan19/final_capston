package com.example.myapplication.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.ResponseStateEvent
import com.example.myapplication.response.ResponseStateResult
import com.example.myapplication.data.DefaultViewModel
import com.example.myapplication.data.repos.AuthRepo
import com.example.myapplication.model.Login
import com.example.myapplication.util.isEmailValid
import com.example.myapplication.util.isTextValid
import com.google.firebase.auth.FirebaseUser

class LoginViewModel:DefaultViewModel() {

    private val authReposetry=AuthRepo()
    private val _isLoged=MutableLiveData<ResponseStateEvent<FirebaseUser>>()


    var isLoged:LiveData<ResponseStateEvent<FirebaseUser>> = _isLoged
    val emailText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()
    val isLoggingIn = MutableLiveData<Boolean>()

    private fun login() {
        isLoggingIn.value = true
        val login = Login(emailText.value!!, passwordText.value!!)

        authReposetry.loginUser(login) { result: ResponseStateResult<FirebaseUser> ->
            onResult(null, result)
            if (result is ResponseStateResult.Success) _isLoged.value = ResponseStateEvent(result.data!!)
            if (result is ResponseStateResult.Success || result is ResponseStateResult.Error) isLoggingIn.value = false
        }
    }
    fun loginPressed() {
        if (!isEmailValid(emailText.value.toString())) {
            mSnackBarText.value = ResponseStateEvent("Invalid email format")
            return
        }

        if (!isTextValid(6, passwordText.value)) {
            mSnackBarText.value = ResponseStateEvent("Password is too short")
            return
        }

        login()
    }


}
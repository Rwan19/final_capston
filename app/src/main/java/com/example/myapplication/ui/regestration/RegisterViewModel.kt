package com.example.myapplication.ui.regestration

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.ResponseStateEvent
import com.example.myapplication.response.ResponseStateResult
import com.example.myapplication.data.DefaultViewModel
import com.example.myapplication.data.database.entity.User
import com.example.myapplication.data.repos.AuthRepo
import com.example.myapplication.data.repos.DatabaseRepo
import com.example.myapplication.model.CreatUser
import com.example.myapplication.util.isEmailValid
import com.example.myapplication.util.isTextValid
import com.google.firebase.auth.FirebaseUser

private const val TAG = "RegisterViewModel"
class RegisterViewModel:DefaultViewModel() {
    private val dbRepository = DatabaseRepo()
    private val authRepository = AuthRepo()
    private val mIsCreatedResponseStateEvent = MutableLiveData<ResponseStateEvent<FirebaseUser>>()

    init {
        Log.d(TAG, "Test: ")
    }

    val isCreatedResponseStateEvent: LiveData<ResponseStateEvent<FirebaseUser>> = mIsCreatedResponseStateEvent
    val displayNameText = MutableLiveData<String>()
    val emailText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()
    val isCreatingAccount = MutableLiveData<Boolean>()


    private fun createAccount() {
        isCreatingAccount.value = true
        val createUser = CreatUser(displayNameText.value!!, emailText.value!!, passwordText.value!!)


        authRepository.createUser(createUser) { responseStateResult: ResponseStateResult<FirebaseUser> ->
            onResult(null, responseStateResult)
            Log.d(TAG, "createAccount: $responseStateResult")
            if (responseStateResult is ResponseStateResult.Success) {
                mIsCreatedResponseStateEvent.value = ResponseStateEvent(responseStateResult.data!!)
                dbRepository.updateNewUser(User().apply {
                    info.id = responseStateResult.data.uid
                    info.displayName = createUser.displayName
                    Log.d(TAG,"from reg $this")
                })
            }else{
                Log.d(TAG,"from reg $responseStateResult")
            }
            if (responseStateResult is ResponseStateResult.Success || responseStateResult is ResponseStateResult.Error) isCreatingAccount.value = false
        }
    }

   fun createAccountPressed() {

       if (!isTextValid(2, displayNameText.value)) {
           mSnackBarText.value = ResponseStateEvent("Display name is too short")
         return
        }

        if (!isEmailValid(emailText.value.toString())) {
           mSnackBarText.value = ResponseStateEvent("Invalid email format")
            return
        }
        if (!isTextValid(6, passwordText.value)) {
            mSnackBarText.value = ResponseStateEvent("Password is too short")
            return
        }

        createAccount()
    }
}
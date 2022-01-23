package com.example.myapplication.ui.regestration

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
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
    private val CHANNEL_ID="channel_id"
    private var resources= R.string.notifiction_channel_name.toString()
    init {
        Log.d(TAG, "Test: ")
    }

    val isCreatedResponseStateEvent: LiveData<ResponseStateEvent<FirebaseUser>> = mIsCreatedResponseStateEvent
    val displayNameText = MutableLiveData<String>()
    val emailText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()
    val isCreatingAccount = MutableLiveData<Boolean>()


    @RequiresApi(Build.VERSION_CODES.O)
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
//        showNotification()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(context:Context) {

        val mChannelName=resources
        val mChannelDescription="Channel Description"
        val mChannelImportance= NotificationManager.IMPORTANCE_DEFAULT
        val channel= NotificationChannel(CHANNEL_ID,mChannelName, mChannelImportance).toString()

        val notificationManager=
            ContextCompat.getSystemService(context, NotificationManager::class.java)

        val builder =context.let {
            NotificationCompat.Builder(context.applicationContext ,channel)
                .setTicker("")
                .setSmallIcon(R.drawable.circle_online)
                .setContentTitle("textTitle")
                .setContentText("textContent")
                .setAutoCancel(true)
        }


        val notificationChannel= NotificationChannel(channel,"signup notification",
            NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager?.createNotificationChannel(notificationChannel)
        builder.setChannelId(channel)

        val notification = builder.build()
        notificationManager?.notify(100,notification)

    }


    @RequiresApi(Build.VERSION_CODES.O)
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
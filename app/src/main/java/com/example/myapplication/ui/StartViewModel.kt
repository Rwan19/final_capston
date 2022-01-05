package com.example.myapplication.ui

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.EventObserver
import com.example.myapplication.ResponseStateEvent

class StartViewModel:ViewModel() {
    private val _loginEvent = MutableLiveData<ResponseStateEvent<Unit>>()
    private val _createAccountEvent = MutableLiveData<ResponseStateEvent<Unit>>()

    val loginEvent: LiveData<ResponseStateEvent<Unit>> = _loginEvent
    val createAccountEvent: LiveData<ResponseStateEvent<Unit>> = _createAccountEvent

    fun goToLoginPressed() {
        _loginEvent.value = ResponseStateEvent(Unit)
    }

    fun goToCreateAccountPressed() {
        _createAccountEvent.value = ResponseStateEvent(Unit)
    }
}
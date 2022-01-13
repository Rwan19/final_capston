package com.example.myapplication.ui.sittings

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.ResponseStateEvent
import com.example.myapplication.response.ResponseStateResult
import com.example.myapplication.data.DefaultViewModel
import com.example.myapplication.data.database.FirebaseReferenceValueObserver
import com.example.myapplication.data.database.entity.UserInfo
import com.example.myapplication.data.repos.AuthRepo
import com.example.myapplication.data.repos.DatabaseRepo
import com.example.myapplication.data.repos.StorageRepo


class SettingsViewModelFactory(private val userID: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(userID) as T
    }
}

class SettingsViewModel(private val userID: String) : DefaultViewModel() {

    private val dbRepository: DatabaseRepo = DatabaseRepo()
    private val storageRepository = StorageRepo()
    private val authRepository = AuthRepo()

    private val _userInfo: MutableLiveData<UserInfo> = MutableLiveData()
    val userInfo: LiveData<UserInfo> = _userInfo

    private val _editStatusEvent = MutableLiveData<ResponseStateEvent<Unit>>()
    val editStatusEvent: LiveData<ResponseStateEvent<Unit>> = _editStatusEvent

    private val _editImageEvent = MutableLiveData<ResponseStateEvent<Unit>>()
    val editImageEvent: LiveData<ResponseStateEvent<Unit>> = _editImageEvent

    private val _logoutEvent = MutableLiveData<ResponseStateEvent<Unit>>()
    val logoutEvent: LiveData<ResponseStateEvent<Unit>> = _logoutEvent

    private val firebaseReferenceObserver = FirebaseReferenceValueObserver()

    init {
        loadAndObserveUserInfo()
    }

    override fun onCleared() {
        super.onCleared()
        firebaseReferenceObserver.clear()
    }

    private fun loadAndObserveUserInfo() {
        dbRepository.loadAndObserveUserInfo(userID, firebaseReferenceObserver)
        { result: ResponseStateResult<UserInfo> -> onResult(_userInfo, result) }
    }

    fun changeUserStatus(status: String) {
        dbRepository.updateUserStatus(userID, status)
    }

    fun changeUserImage(byteArray: ByteArray) {
        storageRepository.updateUserProfileImage(userID, byteArray) { result: ResponseStateResult<Uri> ->
            onResult(null, result)
            if (result is ResponseStateResult.Success) {
                dbRepository.updateUserProfileImageUrl(userID, result.data.toString())
            }
        }
    }

    fun changeUserImagePressed() {
        _editImageEvent.value = ResponseStateEvent(Unit)
    }

    fun changeUserStatusPressed() {
        _editStatusEvent.value = ResponseStateEvent(Unit)
    }

    fun logoutUserPressed() {
        authRepository.logoutUser()
        _logoutEvent.value = ResponseStateEvent(Unit)
    }
}

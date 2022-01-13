package com.example.myapplication.ui.chats

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.myapplication.*
import com.example.myapplication.data.DefaultViewModel
import com.example.myapplication.data.database.FirebaseReferenceValueObserver
import com.example.myapplication.data.database.entity.UserFriend
import com.example.myapplication.data.database.entity.UserInfo
import com.example.myapplication.data.repos.Chat
import com.example.myapplication.data.repos.DatabaseRepo
import com.example.myapplication.model.ChatWthUserInfo
import com.example.myapplication.response.ResponseStateResult
import com.example.myapplication.util.addNewItem
import com.example.myapplication.util.updateItemAt
import com.example.myapplication.util.convertTwoUserIDs


class ChatsViewModelFactory(private val myUserID: String) :
    ViewModelProvider.Factory {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatsViewModel(myUserID) as T
    }
}

@RequiresApi(Build.VERSION_CODES.N)
class ChatsViewModel(val myUserID: String) : DefaultViewModel() {

    private val repository: DatabaseRepo = DatabaseRepo()
    private val firebaseReferenceObserverList = ArrayList<FirebaseReferenceValueObserver>()
    private val _updatedChatWithUserInfo = MutableLiveData<ChatWthUserInfo>()
    private val _selectedChat = MutableLiveData<ResponseStateEvent<ChatWthUserInfo>>()

    var selectedChat: LiveData<ResponseStateEvent<ChatWthUserInfo>> = _selectedChat
    val chatsList = MediatorLiveData<MutableList<ChatWthUserInfo>>()

    init {
        chatsList.addSource(_updatedChatWithUserInfo) { newChat ->
            val chat = chatsList.value?.find { it.mChat.info.id == newChat.mChat.info.id }
            if (chat == null) {
                chatsList.addNewItem(newChat)
            } else {
                chatsList.updateItemAt(newChat, chatsList.value!!.indexOf(chat))
            }
        }
        setupChats()
    }

    override fun onCleared() {
        super.onCleared()
        firebaseReferenceObserverList.forEach { it.clear() }
    }


    private fun setupChats() {
        loadFriends()
    }


    private fun loadFriends() {
        repository.loadFriends(myUserID) { responseStateResult: ResponseStateResult<List<UserFriend>> ->
            onResult(null, responseStateResult)
            if (responseStateResult is ResponseStateResult.Success) responseStateResult.data?.forEach { loadUserInfo(it) }
        }
    }


    private fun loadUserInfo(userFriend: UserFriend) {
        repository.loadUserInfo(userFriend.userID) { responseStateResult: ResponseStateResult<UserInfo> ->
            onResult(null, responseStateResult)
            if (responseStateResult is ResponseStateResult.Success) responseStateResult.data?.let { loadAndObserveChat(it) }
        }
    }


    private fun loadAndObserveChat(userInfo: UserInfo) {
        val observer = FirebaseReferenceValueObserver()
        firebaseReferenceObserverList.add(observer)
        repository.loadAndObserveChat(convertTwoUserIDs(myUserID, userInfo.id), observer) { responseStateResult: ResponseStateResult<Chat> ->
            if (responseStateResult is ResponseStateResult.Success) {
                _updatedChatWithUserInfo.value = responseStateResult.data?.let { it -> ChatWthUserInfo(it, userInfo) }
            } else if (responseStateResult is ResponseStateResult.Error) {
                chatsList.value?.let {
                    val newList = mutableListOf<ChatWthUserInfo>().apply { addAll(it) }
                    newList.removeIf{ it2 -> responseStateResult.msg.toString().contains(it2.mUserInfo.id) }
                    chatsList.value = newList
                }
            }
        }
    }

    fun selectChatWithUserInfoPressed(chat: ChatWthUserInfo) {
        _selectedChat.value = ResponseStateEvent(chat)
    }
}
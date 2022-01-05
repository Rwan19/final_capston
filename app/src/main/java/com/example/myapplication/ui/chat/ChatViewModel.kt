package com.example.myapplication.ui.chat

import androidx.lifecycle.*
import com.example.myapplication.ResponseStateResult
import com.example.myapplication.data.DefaultViewModel
import com.example.myapplication.data.database.FirebaseReferenceChildObserver
import com.example.myapplication.data.database.FirebaseReferenceValueObserver
import com.example.myapplication.data.database.entity.Message
import com.example.myapplication.data.database.entity.UserInfo
import com.example.myapplication.data.repos.Chat
import com.example.myapplication.data.repos.DatabaseRepo
import com.example.myapplication.util.addNewItem


class ChatViewModelFactory(private val myUserID: String, private val otherUserID: String, private val chatID: String) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(myUserID, otherUserID, chatID) as T
    }
}

class ChatViewModel(private val myUserID: String, private val otherUserID: String, private val chatID: String) : DefaultViewModel() {

    private val dbRepository: DatabaseRepo = DatabaseRepo()

    private val _otherUser: MutableLiveData<UserInfo> = MutableLiveData()
    private val _addedMessage = MutableLiveData<Message>()

    private val fbRefMessagesChildObserver = FirebaseReferenceChildObserver()
    private val fbRefUserInfoObserver = FirebaseReferenceValueObserver()

    val messagesList = MediatorLiveData<MutableList<Message>>()
    val newMessageText = MutableLiveData<String>()
    val otherUser: LiveData<UserInfo> = _otherUser

    init {
        setupChat()
        checkAndUpdateLastMessageSeen()
    }

    override fun onCleared() {
        super.onCleared()

        fbRefMessagesChildObserver.clear()
        fbRefUserInfoObserver.clear()
    }





    private fun checkAndUpdateLastMessageSeen() {
        dbRepository.loadChat(chatID) { result: ResponseStateResult<Chat> ->
            if (result is ResponseStateResult.Success && result.data != null) {
                result.data.lastMessage.let {
                    if (!it.seen && it.senderID != myUserID) {
                        it.seen = true
                        dbRepository.updateChatLastMessage(chatID, it)
                    }
                }
            }
        }
    }

    private fun setupChat() {
        dbRepository.loadAndObserveUserInfo(otherUserID, fbRefUserInfoObserver) { result: ResponseStateResult<UserInfo> ->
            onResult(_otherUser, result)
            if (result is ResponseStateResult.Success && !fbRefMessagesChildObserver.isObserving()) {
                loadAndObserveNewMessages()
            }
        }
    }

    private fun loadAndObserveNewMessages() {
        messagesList.addSource(_addedMessage) { messagesList.addNewItem(it) }

        dbRepository.loadAndObserveMessagesAdded(
            chatID,
            fbRefMessagesChildObserver
        ) { result: ResponseStateResult<Message> ->
            onResult(_addedMessage, result)
        }
    }

    fun sendMessagePressed() {
        if (!newMessageText.value.isNullOrBlank()) {
            val newMsg = Message(myUserID, newMessageText.value!!)
            dbRepository.updateNewMessage(chatID, newMsg)
            dbRepository.updateChatLastMessage(chatID, newMsg)
            newMessageText.value = null
        }
    }
}
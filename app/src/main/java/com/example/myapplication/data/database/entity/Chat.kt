package com.example.myapplication.data.repos

import com.google.firebase.database.PropertyName
import com.example.myapplication.data.database.entity.Message
data class Chat (
    @get:PropertyName("lastMessage") @set:PropertyName("lastMessage") var lastMessage: Message = Message(),
    @get:PropertyName("info") @set:PropertyName("info") var info: ChatInfo = ChatInfo()
)

data class ChatInfo(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = ""
)
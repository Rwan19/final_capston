package com.example.myapplication.model


import com.example.myapplication.data.database.entity.UserInfo
import com.example.myapplication.data.repos.Chat


class ChatWthUserInfo (
    var mChat: Chat,
    var mUserInfo: UserInfo
)
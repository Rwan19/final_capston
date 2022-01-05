package com.example.myapplication.data.repos

import com.example.myapplication.*
import com.example.myapplication.data.database.FirebaseDataSource
import com.example.myapplication.data.database.FirebaseReferenceChildObserver
import com.example.myapplication.data.database.FirebaseReferenceValueObserver
import com.example.myapplication.data.database.entity.*
import com.example.myapplication.util.wrapSnapshotToArrayList
import com.example.myapplication.util.wrapSnapshotToClass

class DatabaseRepo {
    private val firebaseDatabaseService=FirebaseDataSource()

    fun updateUserStatus(userID:String, status:String) {
        firebaseDatabaseService.updateUserStatus(userID,status)
    }

    fun updateNewMessage(messageID:String, message: Message) {
        firebaseDatabaseService.pushNewMessage(messageID,message)
    }

    fun updateNameUser(user: User){
        firebaseDatabaseService.updateNewUser(user)
    }

    fun updateNewFriend(myUser: UserFriend, otherUser: UserFriend){
        firebaseDatabaseService.updateNewFriend(myUser, otherUser)
    }

    fun updateNewSentRequest(userID: String,userRequest: UserRequest){
        firebaseDatabaseService.updateNewSentRequest(userID, userRequest)
    }

    fun updateNewNotfication(otherUser: String, userNotification: UserNotification){
        firebaseDatabaseService.updateNewNotification(otherUser, userNotification)
    }

    fun updateChatLastMessage(chatID:String, message: Message){
        firebaseDatabaseService.updateLastMessage(chatID,message)
    }

    fun updateNewChat(chat: Chat){
        firebaseDatabaseService.updateNewChat(chat)
    }

    fun updateUserProfileImageUrl(userID: String, url:String){
        firebaseDatabaseService.updateUserProfileImageUrl(userID,url)
    }

    // remove Function

    fun removeNotification(userID: String, notificationId:String){
        firebaseDatabaseService.removeNotification(userID,notificationId)
    }

    fun removeFriend(userID: String, friendId:String){
        firebaseDatabaseService.removeFriend(userID,friendId)
    }

    fun removeSenRequest(otherUser: String, myUserId:String){
        firebaseDatabaseService.removeSentRequest(otherUser,myUserId)
    }

    fun removeChat(chatID: String){
        firebaseDatabaseService.removeChat(chatID)
    }

    fun removeMessages(messageID: String){
        firebaseDatabaseService.removeMessages(messageID)
    }

    //load Function

    fun loadUser(userID: String,b :(ResponseStateResult<User>) -> Unit){
        firebaseDatabaseService.loadUserTask(userID).addOnSuccessListener {
            b.invoke(ResponseStateResult.Success(wrapSnapshotToClass(User::class.java,it)))
        }.addOnFailureListener { b.invoke(ResponseStateResult.Error(it.message)) }
    }

    fun loadUserInfo(userId: String,b :(ResponseStateResult<UserInfo>) -> Unit){
        firebaseDatabaseService.loadUserInfoTask(userId).addOnSuccessListener {
            b.invoke(ResponseStateResult.Success(wrapSnapshotToClass(UserInfo::class.java,it)))
        }.addOnFailureListener { b.invoke(ResponseStateResult.Error(it.message)) }
    }

    fun loadChat(chatId: String, b: (ResponseStateResult<Chat>) -> Unit){
        firebaseDatabaseService.loadUserTask(chatId).addOnSuccessListener {
            b.invoke(ResponseStateResult.Success(wrapSnapshotToClass(Chat::class.java,it)))
        }.addOnFailureListener { b.invoke(ResponseStateResult.Error(it.message)) }
    }



    fun loadUser(b: (ResponseStateResult<MutableList<User>>) -> Unit){
        b.invoke(ResponseStateResult.Loading)
        firebaseDatabaseService.loadUsersTask().addOnSuccessListener {
            val usersList = wrapSnapshotToArrayList(User::class.java,it)
            b.invoke(ResponseStateResult.Success(usersList))
        }.addOnFailureListener { b.invoke(ResponseStateResult.Error(it.message)) }
    }


    fun loadFriends(userID: String, b: ((ResponseStateResult<List<UserFriend>>) -> Unit)) {
        b.invoke(ResponseStateResult.Loading)
        firebaseDatabaseService.loadFriendsTask(userID).addOnSuccessListener {
            val friendsList = wrapSnapshotToArrayList(UserFriend::class.java, it)
            b.invoke(ResponseStateResult.Success(friendsList))
        }.addOnFailureListener { b.invoke(ResponseStateResult.Error(it.message)) }
    }

    fun loadNotification(userID:String,b: (ResponseStateResult<MutableList<User>>) -> Unit){
        b.invoke(ResponseStateResult.Loading)
        firebaseDatabaseService.loadFriendsTask(userID).addOnSuccessListener {
            val notificationList = wrapSnapshotToArrayList(User::class.java,it)
            b.invoke(ResponseStateResult.Success(notificationList))
        }.addOnFailureListener { b.invoke(ResponseStateResult.Error(it.message)) }
    }

    fun loadAndObserveUser(userID: String, observer: FirebaseReferenceValueObserver, b: ((ResponseStateResult<User>) -> Unit)) {
        firebaseDatabaseService.attachUserObserver(User::class.java, userID, observer, b)
    }

    fun loadAndObserveUserInfo(userID: String, observer: FirebaseReferenceValueObserver, b: ((ResponseStateResult<UserInfo>) -> Unit)) {
        firebaseDatabaseService.attachUserInfoObserver(UserInfo::class.java, userID, observer, b)
    }

    fun loadAndObserveUserNotifications(userID: String, observer: FirebaseReferenceValueObserver, b: ((ResponseStateResult<MutableList<UserNotification>>) -> Unit)){
        firebaseDatabaseService.attachUserNotificationsObserver(UserNotification::class.java, userID, observer, b)
    }

    fun loadAndObserveMessagesAdded(messagesID: String, observer: FirebaseReferenceChildObserver, b: ((ResponseStateResult<Message>) -> Unit)) {
        firebaseDatabaseService.attachMessagesObserver(Message::class.java, messagesID, observer, b)
    }

    fun loadAndObserveChat(chatID: String, observer: FirebaseReferenceValueObserver, b: ((ResponseStateResult<Chat>) -> Unit)) {
        firebaseDatabaseService.attachChatObserver(Chat::class.java, chatID, observer, b)
    }

}
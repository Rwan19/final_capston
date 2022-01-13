package com.example.myapplication.ui.user

import androidx.lifecycle.*
import com.example.myapplication.ResponseStateEvent
import com.example.myapplication.data.DefaultViewModel
import com.example.myapplication.data.database.entity.User
import com.example.myapplication.data.repos.DatabaseRepo
import com.example.myapplication.response.ResponseStateResult


class UsersViewModelFactory(private val myUserID: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UsersViewModel(myUserID) as T
    }
}

class UsersViewModel(private val myUserID: String) : DefaultViewModel() {
    private val repository: DatabaseRepo = DatabaseRepo()

    private val _selectedUser = MutableLiveData<ResponseStateEvent<User>>()
    var selectedUser: LiveData<ResponseStateEvent<User>> = _selectedUser
    private val updatedUsersList = MutableLiveData<MutableList<User>>()
    val usersList = MediatorLiveData<List<User>>()

    init {
        usersList.addSource(updatedUsersList) { mutableList ->
            usersList.value = updatedUsersList.value?.filter { it.info.id != myUserID }
        }
        loadUsers()
    }

    private fun loadUsers() {
        repository.loadUsers { result: ResponseStateResult<MutableList<User>> ->
            onResult(updatedUsersList, result)
        }
    }

    fun selectUser(user: User) {
        _selectedUser.value = ResponseStateEvent(user)
    }
}
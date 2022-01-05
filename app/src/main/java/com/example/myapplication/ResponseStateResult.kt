package com.example.myapplication


sealed class ResponseStateResult<out R> {
    data class Success<out T>(val data: T? = null, val msg: String? = null) : ResponseStateResult<T>()
    class Error(val msg: String? = null) : ResponseStateResult<Nothing>()
    object Loading : ResponseStateResult<Nothing>()
}

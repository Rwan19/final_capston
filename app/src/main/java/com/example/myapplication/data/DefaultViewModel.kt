package com.example.myapplication.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.ResponseStateEvent
import com.example.myapplication.response.ResponseStateResult

abstract class DefaultViewModel:ViewModel() {

    protected val mSnackBarText = MutableLiveData<ResponseStateEvent<String>>()
    val snackBarText: LiveData<ResponseStateEvent<String>> = mSnackBarText

    private val mDataLoading = MutableLiveData<ResponseStateEvent<Boolean>>()
    val dataLoading: LiveData<ResponseStateEvent<Boolean>> = mDataLoading

    protected fun <T> onResult(mutableLiveData: MutableLiveData<T>? = null, responseStateResult: ResponseStateResult<T>) {
        when (responseStateResult) {
            is ResponseStateResult.Loading -> mDataLoading.value = ResponseStateEvent(true)

            is ResponseStateResult.Error -> {
                mDataLoading.value = ResponseStateEvent(false)
                responseStateResult.msg?.let { mSnackBarText.value = ResponseStateEvent(it) }
            }

            is ResponseStateResult.Success -> {
                mDataLoading.value = ResponseStateEvent(false)
                responseStateResult.data?.let { mutableLiveData?.value = it }
                responseStateResult.msg?.let { mSnackBarText.value = ResponseStateEvent(it) }
            }
        }
    }

}
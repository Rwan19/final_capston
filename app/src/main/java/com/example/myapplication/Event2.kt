package com.example.myapplication

import androidx.lifecycle.Observer


open class ResponseStateEvent<out T>
    (private val content :T) {
    private var isHandled= false

    fun getContentIfNotHandled():T?{
        return if (isHandled){
            null
        }else{
            isHandled=true
            content
        }
    }
}


class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<ResponseStateEvent<T>> {
    override fun onChanged(responseStateEvent: ResponseStateEvent<T>?) {
        responseStateEvent?.getContentIfNotHandled()?.let { onEventUnhandledContent(it) }
    }
}
package com.example.myapplication.util

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.ui.sittings.SittingsFragment

object SharedPreferencesUtil {
    private const val PACKAGE_NAME = "com.myapplication.android_chat_app"
    private const val KEY_USER_ID = "user_info"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)
    }

    fun getUserID(context: Context): String? {
        return getPrefs(context).getString(KEY_USER_ID, null)
    }

    fun saveUserID(context: Context, userID: String) {
        getPrefs(context).edit().putString(KEY_USER_ID, userID).apply()
    }

    fun removeUserID(context: Context) {
        getPrefs(context).edit().remove(KEY_USER_ID).apply()
    }


}
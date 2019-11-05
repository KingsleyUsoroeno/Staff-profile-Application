package com.techgroup.social_vue.data

import android.content.Context
import android.content.SharedPreferences


object SharedPreferenceHelper {

    private const val PREF_FILE = "preference_file"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
    }

    private fun getEditor(context: Context): SharedPreferences.Editor {
        return getPreferences(context).edit()
    }

    fun setString(context: Context, key: String, value: String) {
        if (key.isNotEmpty()) {
            getEditor(context).putString(key, value).apply()
        }
    }

    fun setInt(context: Context, key: String, value: Int) {
        getEditor(context).putInt(key, value).apply()
    }

    fun setBoolean(context: Context, key: String, value: Boolean) {
        getEditor(context).putBoolean(key, value).apply()
    }

    fun getString(context: Context, key: String, defValue: String): String? {
        return getPreferences(context).getString(key, defValue)
    }

    fun getBoolean(context: Context, key: String, defValue: Boolean): Boolean {
        return getPreferences(context).getBoolean(key, defValue)
    }


    fun getInt(context: Context, key: String, defValue: Int): Int {
        return getPreferences(context).getInt(key, defValue)
    }
}
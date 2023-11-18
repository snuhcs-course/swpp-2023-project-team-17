package com.example.goclass.ui.mainui.profile.utils

import android.content.Context

object SharedPrefsUtils {
    fun save(context: Context, key: String, value: Any?) {
        val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                else -> throw IllegalArgumentException("Not a valid type to save in Shared Preferences")
            }
            apply()
        }
    }

    fun get(context: Context, key: String, defaultValue: Any): Any? {
        val sharedPrefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return when (defaultValue) {
            is String -> sharedPrefs.getString(key, defaultValue)
            is Int -> sharedPrefs.getInt(key, defaultValue)
            is Boolean -> sharedPrefs.getBoolean(key, defaultValue)
            is Float -> sharedPrefs.getFloat(key, defaultValue)
            is Long -> sharedPrefs.getLong(key, defaultValue)
            else -> throw IllegalArgumentException("Invalid type")
        }
    }

    fun clear(context: Context) {
        val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
    }

}
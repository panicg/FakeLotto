package com.panicdev.panic.common

import android.content.SharedPreferences
import android.preference.PreferenceManager


open class PreferenceController {

    private val preferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        preferences = PreferenceManager.getDefaultSharedPreferences(CommonApplication.appContext).apply {
            editor = edit()
        }
    }

    fun putString(key: String, value: String?) {
        editor.putString(key, value)
        editor.commit()
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return preferences.getString(key, defaultValue)
    }

    fun putInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.commit()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }

    fun putFloat(key: String, value: Float) {
        editor.putFloat(key, value)
        editor.commit()
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return preferences.getFloat(key, defaultValue)
    }

    fun putLong(key: String, value: Long) {
        editor.putLong(key, value)
        editor.commit()
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return preferences.getLong(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun remove(key: String) {
        editor.remove(key)
        editor.commit()
    }

    fun clear() {
        editor.clear()
        editor.commit()
    }

    companion object {

        private var _instance: PreferenceController? = null

        val instance: PreferenceController
            get() {
                if (_instance == null) _instance = PreferenceController()
                return _instance as PreferenceController
            }
    }
}

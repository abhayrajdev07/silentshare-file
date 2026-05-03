package com.example.silentshare

import android.content.Context

object PreferenceManager {

    private const val PREF_NAME = "silentshare_prefs"
    private const val KEY_DIALOG_SHOWN = "offline_dialog_shown"

    fun isDialogShown(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_DIALOG_SHOWN, false)
    }

    fun setDialogShown(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_DIALOG_SHOWN, true).apply()
    }
}
package io.github.nhths.notionlivewallpaper.data.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import io.github.nhths.notionlivewallpaper.data.StorageUtils

class KeyStoreSharedPreferences (val sharedPreferences: SharedPreferences) : KeyStore() {

    override fun getToken(handler: (String) -> Unit) {
        handler(sharedPreferences.getString(StorageUtils.PREF_KEY_TOKEN, "")!!)
    }

    override fun writeToken(token: String) {
        sharedPreferences.edit {
            putString(StorageUtils.PREF_KEY_TOKEN, token)
        }
    }

    override fun removeToken() {
        sharedPreferences.edit {
            remove(StorageUtils.PREF_KEY_TOKEN)
        }
    }
}
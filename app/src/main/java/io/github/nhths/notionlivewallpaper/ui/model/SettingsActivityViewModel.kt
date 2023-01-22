package io.github.nhths.notionlivewallpaper.ui.model

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.nhths.notionlivewallpaper.App
import io.github.nhths.notionlivewallpaper.data.notion.auth.Auth

class SettingsActivityViewModel(application: Application) : AndroidViewModel(application) {

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsActivityViewModel(application) as T
        }
    }

    fun parseIntent(intent: Intent){
        val auth = Auth()

        val appLinkIntent = intent
        val appLinkAction = appLinkIntent.action
        val appLinkData = appLinkIntent.data

        Log.i("Intents", appLinkData.toString() + "  " + appLinkIntent.toString() + "  " + appLinkAction.toString())

        val code: String? = appLinkData?.getQueryParameter("code")

        Log.i("Login", code.orEmpty())

        if (code == null) {
            val browserIntent = Intent(Intent.ACTION_VIEW, auth.getAuthCodeUri())
            browserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            getApplication<App>().applicationContext.startActivity(browserIntent)

        } else {
            auth.getToken(code)
        }
    }

}
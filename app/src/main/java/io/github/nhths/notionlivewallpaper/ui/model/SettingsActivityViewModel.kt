package io.github.nhths.notionlivewallpaper.ui.model

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.nhths.notionlivewallpaper.App
import io.github.nhths.notionlivewallpaper.data.StorageUtils
import io.github.nhths.notionlivewallpaper.data.auth.*

class SettingsActivityViewModel(application: Application) : AndroidViewModel(application) {

    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsActivityViewModel(application) as T
        }
    }

    private fun startAuth(){
        Auth.Instance.authWithPublicIntegration(
            NotionOAuth.getAuthCodeUri().toString(),
            object : Auth.TokenStateListener{
            override fun onCodeNeedRequest(requestUri: Uri) {
                val browserIntent = Intent(Intent.ACTION_VIEW, requestUri)
                browserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                getApplication<App>().applicationContext.startActivity(browserIntent)
            }

            override fun onTokenNeedRequest(code: String) {
                val request = NotionOAuth.getTokenAuthRequest(AuthData(), code)
                NotionOAuth.sendPostTokenRequest(request,
                    {e -> Log.e("TokenResponseErr", e.toString())}
                ) { responseBody, responseCode, msg ->
                    Log.i("TokenResponse", "$responseCode, $msg, ${responseBody.accessToken}")
                    Auth.Instance.receivedToken(responseBody.accessToken)
                }
            }

        })
    }
    fun parseIntent(intent: Intent) {
        if (Auth.Instance.state.value == Auth.State.INIT) {
            Auth.Instance.findToken(
                object : Auth.KeyStoreOwner {
                    val keyStore = KeyStoreSharedPreferences(
                        getApplication<App>().getSharedPreferences(StorageUtils.PREF_STORAGE_KEY, Context.MODE_PRIVATE)
                    )
                    override fun getKeyStore(): KeyStore = keyStore
                },
                object : Auth.AuthStateListener {
                    override fun onAuthNeed() {
                        Log.i("Auth", "Need auth")
                        startAuth()
                    }

                    override fun onTokenExpired() {
                        Log.i("Auth", "Token Expired")
                    }

                    override fun onFindToken(token: String) {
                        Log.i("Auth", "Found token")
                    }
                }
            )
        }

        if (intent.action != Intent.ACTION_VIEW || !intent.categories.contains(Intent.CATEGORY_BROWSABLE)) {
            return
        }
        val appLinkData = intent.data
        if (Auth.Instance.state.value == Auth.State.WAIT_CODE) {
            appLinkData?.getQueryParameter("code")?.let {Auth.Instance.receivedCode(it)}
        }
    }

}
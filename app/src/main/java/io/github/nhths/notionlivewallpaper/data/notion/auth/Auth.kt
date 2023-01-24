package io.github.nhths.notionlivewallpaper.data.notion.auth

import android.net.Uri
import android.util.Log
import io.github.nhths.notionlivewallpaper.Config
import io.github.nhths.notionlivewallpaper.data.notion.NetwokUtils
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class Auth() {

    fun getAuthCodeUri() : Uri{
        val uri = Uri.parse("${Config.OAuthCodeUrl}?client_id=${Config.OAuthClientID}&response_type=code&owner=user&redirect_uri=${Config.OAuthRedirectUrl}")
        return uri
    }

    fun getToken(code:String){
        val client = OkHttpClient()
        val rbody : RequestBody = """
                {   
                    "grant_type" : "authorization_code",
                    "redirect_uri" : "${Config.OAuthRedirectUrl}",
                    "code" : "$code"
                }
            """.toRequestBody(NetwokUtils.MediaTypes.JSON.mediaType())

        Log.i("Token", NetwokUtils.makeBasicAuthCredentials(Config.OAuthClientID, Config.OAuthClientSecret))

        val request = Request.Builder()
            .url(Config.OAuthTokenUrl.toHttpUrl())
            .addHeader("Authorization", NetwokUtils.makeBasicAuthCredentials(Config.OAuthClientID, Config.OAuthClientSecret))
            .addHeader("Content-Type", NetwokUtils.MediaTypes.JSON.getTypeString())
            .post(rbody)
            .build()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Token", "Error $e, $call")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.i("Token", "Success!!! $response; ${response.body?.string()}")
            }

        })
    }

}
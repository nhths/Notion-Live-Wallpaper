package io.github.nhths.notionlivewallpaper.data.auth

import android.net.Uri
import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.github.nhths.notionlivewallpaper.Config
import io.github.nhths.notionlivewallpaper.data.NetwokUtils
import io.github.nhths.notionlivewallpaper.data.model.AuthTokenRequestModel
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

        val authBody = AuthTokenRequestModel(Config.OAuthRedirectUrl, code)

        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<AuthTokenRequestModel> = moshi.adapter(AuthTokenRequestModel::class.java)

        val rbody : RequestBody = jsonAdapter.toJson(authBody).toRequestBody(NetwokUtils.MediaTypes.JSON.mediaType())

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
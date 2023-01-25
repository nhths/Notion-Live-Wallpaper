package io.github.nhths.notionlivewallpaper.data.auth

import android.net.Uri
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.github.nhths.notionlivewallpaper.Config
import io.github.nhths.notionlivewallpaper.data.NetwokUtils
import io.github.nhths.notionlivewallpaper.data.model.AuthTokenRequestModel
import io.github.nhths.notionlivewallpaper.data.model.AuthTokenResponseModel
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class NotionOAuth {
    companion object {
        fun getAuthCodeUri(): Uri =
            Uri.parse(Config.OAuthCodeUrl)
                .buildUpon()
                .appendQueryParameter("client_id", Config.OAuthClientID)
                .appendQueryParameter("redirect_uri", Config.OAuthRedirectUrl)
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("owner", "user")
                .build()

        fun getTokenAuthRequest(authData: AuthData, code: String) : Request {
            val moshi: Moshi = Moshi.Builder().build()
            val requestJsonAdapter: JsonAdapter<AuthTokenRequestModel> = moshi.adapter(
                AuthTokenRequestModel::class.java)


            val authBody = AuthTokenRequestModel(authData.OAuthRedirectUrl, code)
            val requestBody : RequestBody = requestJsonAdapter.toJson(authBody).toRequestBody(NetwokUtils.MediaTypes.JSON.mediaType())

            return Request.Builder()
                .url(Config.OAuthTokenUrl.toHttpUrl())
                .addHeader("Authorization", NetwokUtils.makeBasicAuthCredentials(authData.OAuthClientID, authData.OAuthClientSecret))
                .addHeader("Content-Type", NetwokUtils.MediaTypes.JSON.getTypeString())
                .post(requestBody)
                .build()
        }


        fun sendPostTokenRequest(
            request: Request,
            callbackErr: (IOException) -> Unit,
            callback: (AuthTokenResponseModel, Int, String) -> Unit
        ){
            val client = OkHttpClient()

            val moshi: Moshi = Moshi.Builder().build()
            val responseJsonAdapter: JsonAdapter<AuthTokenResponseModel> = moshi.adapter(
                AuthTokenResponseModel::class.java)

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callbackErr(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = response.body?.string()
                    json?.let {
                        callback(responseJsonAdapter.fromJson(json)!!, response.code, response.message)
                    }
                }

            })
        }
    }
}
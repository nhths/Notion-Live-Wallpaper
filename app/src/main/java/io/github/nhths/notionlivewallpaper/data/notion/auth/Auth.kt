package io.github.nhths.notionlivewallpaper.data.notion.auth

import android.net.Uri
import android.util.Base64
import android.util.Log
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class Auth() {

    fun getAuthCodeUri() : Uri{
        val client_id = "379b996e-fd9f-407e-a2cb-b26f1948de38"
        val redirect_uri = "https%3A%2F%2Fnhths.github.io%2FNotion-Live-Wallpaper%2Fauth.html"
        val uri = Uri.parse("https://api.notion.com/v1/oauth/authorize?client_id=$client_id&response_type=code&owner=user&redirect_uri=$redirect_uri")
        return uri
    }

    fun getToken(code:String){
        val client = OkHttpClient()
        val httpBuilder = "https://api.notion.com/v1/oauth/token".toHttpUrlOrNull()!!
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        val rbody : RequestBody = """
                {   
                    "grant_type" : "authorization_code",
                    "redirect_uri" : "https://nhths.github.io/Notion-Live-Wallpaper/auth.html",
                    "code" : "$code"
                }
            """.toRequestBody(JSON)

        val cred = "379b996e-fd9f-407e-a2cb-b26f1948de38:secret_r9KVBmG9LTnJP3gO8iWxUnh9YEtljC1xA9QTkSuGAwI"
        val b64 = Base64.encode(cred.toByteArray(), Base64.NO_WRAP)

        val resString = b64.toString(Charsets.UTF_8)

        Log.i("Credentials", "Oauth 'client:secret' base 64: $resString")

        val request = Request.Builder()
            .url(httpBuilder)
            .addHeader("Authorization", "Basic $resString")
            .addHeader("Content-Type", "application/json")
            .post(rbody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Token", "Error $e, $call");
            }

            override fun onResponse(call: Call, response: Response) {
                Log.i("Token", "Success!!! $response; ${response.body?.string()}")
            }

        })
    }

}
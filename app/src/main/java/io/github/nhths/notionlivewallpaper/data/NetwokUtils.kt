package io.github.nhths.notionlivewallpaper.data

import android.util.Base64
import okhttp3.MediaType.Companion.toMediaType

class NetwokUtils {

    enum class MediaTypes(private val type: String) {
        JSON("application/json; charset=utf-8");

        fun mediaType() = type.toMediaType()
        fun getTypeString() = type
    }

    companion object {

        fun makeBasicAuthCredentials(client_id: String, client_secret: String) =
            "Basic " + Base64.encode(
                "$client_id:$client_secret".toByteArray(),
                Base64.NO_WRAP
            ).toString(Charsets.UTF_8)

    }
}
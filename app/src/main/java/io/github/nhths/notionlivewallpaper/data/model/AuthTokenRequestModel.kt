package io.github.nhths.notionlivewallpaper.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthTokenRequestModel (
    @field:Json(name = "redirect_uri")
    val redirectUri: String,

    @field:Json(name = "code")
    val code: String,

    @field:Json(name = "grant_type")
    val grantType: String = "authorization_code"
)

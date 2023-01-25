package io.github.nhths.notionlivewallpaper.data.auth

import io.github.nhths.notionlivewallpaper.Config
import java.net.URL

data class AuthData(
    val OAuthCodeUrl: String = Config.OAuthCodeUrl,

    val OAuthTokenUrl: String = Config.OAuthTokenUrl,

    val OAuthClientID: String = Config.OAuthClientID,

    val OAuthClientSecret: String = Config.OAuthClientSecret,

    val OAuthRedirectUrl: String = URL(Config.OAuthRedirectUrl).toString(),
)
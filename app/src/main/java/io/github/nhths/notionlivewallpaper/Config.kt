package io.github.nhths.notionlivewallpaper

import notion.api.v1.model.error.OAuthError
import java.net.URL

class Config {
    companion object {
        const val OAuthCodeUrl = "https://api.notion.com/v1/oauth/authorize"

        const val OAuthTokenUrl = "https://api.notion.com/v1/oauth/token"

        const val OAuthClientID = BuildConfig.OAuthClientID

        const val OAuthClientSecret = BuildConfig.OAuthClientSecret

        val OAuthRedirectUrl = URL(BuildConfig.OAuthRedirectUri).toString()
    }
}
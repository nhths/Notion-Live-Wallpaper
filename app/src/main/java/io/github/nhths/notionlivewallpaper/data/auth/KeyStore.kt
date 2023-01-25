package io.github.nhths.notionlivewallpaper.data.auth

abstract class KeyStore {
    abstract fun getToken(handler: (String) -> Unit)

    abstract fun writeToken(token: String)

    abstract fun removeToken()
}
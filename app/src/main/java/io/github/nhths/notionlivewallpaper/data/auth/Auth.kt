package io.github.nhths.notionlivewallpaper.data.auth

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class Auth private constructor() {

    companion object {
        val Instance = Auth()
    }

    private var authStateListener: AuthStateListener? = null
    private var tokenStateListener: TokenStateListener? = null

    private lateinit var keyStoreOwner: KeyStoreOwner

    enum class State {
        INIT,
        NEED_AUTH,
        WAIT_CODE,
        WAIT_TOKEN,
        SUCCESS
    }

    data class StateHolder (
        val state: State,
        val value: String = ""
    )

    private val stateObserver = Observer<StateHolder> { holder ->
        Log.i("Auth State", "Auth state: ${holder.state}")
        when(holder.state){
            State.INIT -> {}
            State.NEED_AUTH -> {
                authStateListener?.let { it.onAuthNeed() }
            }
            State.WAIT_CODE -> {
                tokenStateListener?.let { it.onCodeNeedRequest(Uri.parse(holder.value)) }
            }
            State.WAIT_TOKEN -> {
                tokenStateListener?.let { it.onTokenNeedRequest(holder.value) }
            }
            State.SUCCESS -> {
                authStateListener?.let { it.onFindToken(holder.value) }
            }
        }
    }

    private val stateMapper = Observer<StateHolder> { _state.value = it.state }

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    private val _stateHolder = MutableLiveData<StateHolder>(StateHolder(State.INIT)).apply {
        observeForever(stateObserver)
        observeForever(stateMapper)
    }

    interface KeyStoreOwner {
        fun getKeyStore(): KeyStore
    }

    interface TokenStateListener {
        fun onCodeNeedRequest(requestUri: Uri)

        fun onTokenNeedRequest(code: String)
    }

    interface AuthStateListener {
        fun onAuthNeed()

        fun onTokenExpired()

        fun onFindToken(token: String)
    }

    fun authWithPublicIntegration(requestUri: String, tokenStateListener: TokenStateListener) {
        this.tokenStateListener = tokenStateListener
        _stateHolder.postValue(StateHolder(State.WAIT_CODE, requestUri))
    }

    fun authWithPrivateIntegration(token: String) {
        keyStoreOwner.getKeyStore().writeToken(token)
        _stateHolder.postValue(StateHolder(State.SUCCESS, token))
    }

    fun updateToken() {
        _stateHolder.postValue(StateHolder(State.NEED_AUTH))
    }

    fun findToken(keyStoreOwner: KeyStoreOwner, authStateListener: AuthStateListener){
        this.keyStoreOwner = keyStoreOwner
        this.authStateListener = authStateListener

        keyStoreOwner.getKeyStore().getToken {
            if (it.isEmpty()){
                _stateHolder.postValue(StateHolder(State.NEED_AUTH))
            } else {
                _stateHolder.postValue(StateHolder(State.SUCCESS, it))
            }
        }
    }

    fun receivedCode(code: String) {
        if (state.value != State.WAIT_CODE) {
            throw IllegalStateException("Authenticator does not need a code!!!")
        }
        _stateHolder.postValue(StateHolder(State.WAIT_TOKEN, code))
        authStateListener
    }

    fun receivedToken(token: String) {
        if (state.value != State.WAIT_TOKEN) {
            throw IllegalStateException("Authenticator does not need a token!!!")
        }
        keyStoreOwner.getKeyStore().writeToken(token)
        _stateHolder.postValue(StateHolder(State.SUCCESS, token))
    }

    fun logout() {
        keyStoreOwner.getKeyStore().removeToken()
        _stateHolder.value = StateHolder(State.NEED_AUTH)
    }
}
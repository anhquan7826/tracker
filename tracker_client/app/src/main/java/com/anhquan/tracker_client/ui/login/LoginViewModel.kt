package com.anhquan.tracker_client.ui.login

import androidx.lifecycle.ViewModel
import com.anhquan.tracker_client.utils.errorLog
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    enum class LoginState {
        NONE, LOGGED_IN, INVALID_EMAIL, INVALID_PASSWORD, EXCEPTION
    }

    private val _logIn = MutableStateFlow(LoginState.NONE)
    val loggedIn = _logIn.asStateFlow()

    fun login(email: String, password: String) {
        if (!isValidEmail(email)) {
            _logIn.update { LoginState.INVALID_EMAIL }
            return
        }
        if (password.length < 6) {
            _logIn.update { LoginState.INVALID_PASSWORD }
            return
        }
        Firebase.auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            _logIn.update { LoginState.LOGGED_IN }
        }.addOnFailureListener {
            errorLog(it)
            _logIn.update { LoginState.EXCEPTION }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$").matches(email)
    }
}
package com.anhquan.tracker_client.ui.signUp

import androidx.lifecycle.ViewModel
import com.anhquan.tracker_client.utils.errorLog
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignUpViewModel : ViewModel() {
    enum class SignInState {
        NONE, SIGNED_UP, INVALID_EMAIL, INVALID_PASSWORD, PASSWORD_NOT_MATCH, EXCEPTION
    }

    private var _signUp = MutableStateFlow(SignInState.NONE)
    val signUp = _signUp.asStateFlow()

    fun signUp(email: String, password: String, rePassword: String) {
        if (!isValidEmail(email)) {
            _signUp.update { SignInState.INVALID_EMAIL }
            return
        }
        if (password.length < 6) {
            _signUp.update { SignInState.INVALID_PASSWORD }
            return
        }
        if (password != rePassword) {
            _signUp.update { SignInState.PASSWORD_NOT_MATCH }
            return
        }
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _signUp.update { SignInState.SIGNED_UP }
            }
            .addOnFailureListener {
                _signUp.update { SignInState.EXCEPTION }
                errorLog(it)
            }
    }

    private fun isValidEmail(email: String): Boolean {
        return Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$").matches(email)
    }
}
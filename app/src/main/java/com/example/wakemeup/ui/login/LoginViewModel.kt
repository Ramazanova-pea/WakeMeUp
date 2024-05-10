package com.example.wakemeup.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wakemeup.ui.signup.RegistrationState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginViewModel : ViewModel() {
    private val _loginState = MutableLiveData<LoginState>()
    val loginState: MutableLiveData<LoginState>
        get() = _loginState

    fun onLoginClick(email: String, password: String) {
        if (!checkEmail(email)) _loginState.value = LoginState.ERROR_INVALID_EMAIL
        else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _loginState.value = LoginState.SUCCESS
                    } else {
                        when (task.exception) {
                            is FirebaseAuthUse
                            is FirebaseAuthInvalidUserException -> _loginState.value =
                                LoginState.ERROR_USER_DOESNT_EXIST

                            is FirebaseAuthInvalidCredentialsException -> _loginState.value =
                                LoginState.ERROR_WRONG_PASSWORD

                            else -> _loginState.value = LoginState.ERROR
                        }
                    }
                }
        }
    }

    fun checkEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}


enum class LoginState {
    SUCCESS,
    ERROR_INVALID_EMAIL,
    ERROR_USER_DOESNT_EXIST,
    ERROR_WRONG_PASSWORD,
    ERROR
}
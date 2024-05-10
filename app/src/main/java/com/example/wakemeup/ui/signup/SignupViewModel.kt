package com.example.wakemeup.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class SignupViewModel : ViewModel() {

    private val _registrationState = MutableLiveData<RegistrationState>()
    val registrationState: MutableLiveData<RegistrationState>
        get() = _registrationState

    fun onSignUpClick(name: String, email: String, password: String) {
        if (!checkEmail(email)) _registrationState.value = RegistrationState.ERROR_INVALID_EMAIL
        else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _registrationState.value = RegistrationState.SUCCESS
                    } else {
                        when (task.exception) {
                            is FirebaseAuthUserCollisionException -> _registrationState.value =
                                RegistrationState.ERROR_USER_ALREADY_EXISTS

                            is FirebaseAuthWeakPasswordException -> _registrationState.value =
                                RegistrationState.ERROR_WEAK_PASSWORD

                            is FirebaseAuthInvalidCredentialsException -> _registrationState.value =
                                RegistrationState.ERROR_INVALID_CREDENTIALS

                            else -> _registrationState.value = RegistrationState.ERROR
                        }
                    }
                }
        }
    }

    fun checkEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

enum class RegistrationState {
    SUCCESS,
    ERROR_USER_ALREADY_EXISTS,
    ERROR_WEAK_PASSWORD,
    ERROR_INVALID_CREDENTIALS,
    ERROR_INVALID_EMAIL,
    ERROR
}
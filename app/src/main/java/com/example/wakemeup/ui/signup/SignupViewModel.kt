package com.example.wakemeup.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SignupViewModel : ViewModel() {

    private val _registrationState = MutableLiveData<RegistrationState>()
    val registrationState: MutableLiveData<RegistrationState>
        get() = _registrationState
    fun onSignUpClick(name: String, email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _registrationState.value = RegistrationState.SUCCESS
                } else {
                    _registrationState.value = RegistrationState.ERROR
                }
            }
    }

    fun checkEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

//    fun checkPhone(phone: String): Boolean {
//        return android.util.Patterns.PHONE.matcher(phone).matches()
//    }
}

enum class RegistrationState {
    SUCCESS,
    ERROR
}
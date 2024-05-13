package com.example.wakemeup.ui.authentication.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {

    fun onLoginClick(email: String, password: String, context: Context): Flow<LoginState> = flow {
        emit(LoginState.LOADING)
        var state = LoginState.ERROR
        try {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    state = if (task.isSuccessful) {
                        LoginState.SUCCESS
                    } else {
                        LoginState.ERROR
                    }
                }.await()
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            state = LoginState.ERROR_WRONG_CREDENTIALS
        }
        emit(state)
    }.flowOn(Dispatchers.IO)
}


enum class LoginState {
    SUCCESS,
    ERROR_WRONG_CREDENTIALS,
    ERROR,
    LOADING
}
package com.example.wakemeup.ui.authentication.signup

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wakemeup.data.UsersRepositoryImpl
import com.example.wakemeup.domain.CreateUserUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class SignupViewModel : ViewModel() {

    private var userImage: Bitmap? = null

    fun onSignUpClick(
        name: String,
        email: String,
        password: String,
        phoneNumber: String,
        context: Context
    ): Flow<RegistrationState> = flow {
        var state: RegistrationState = RegistrationState.ERROR
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    state = RegistrationState.SUCCESS
                } else {
                    when (task.exception) {
                        is FirebaseAuthUserCollisionException -> state =
                            RegistrationState.ERROR_USER_ALREADY_EXISTS

                        is FirebaseAuthWeakPasswordException -> state =
                            RegistrationState.ERROR_WEAK_PASSWORD

                        is FirebaseAuthInvalidCredentialsException -> state =
                            RegistrationState.ERROR_INVALID_CREDENTIALS
                    }
                }
            }.await()
        if (state != RegistrationState.SUCCESS) {
            emit(state)
            return@flow
        }
        CreateUserUseCase.execute(
            FirebaseAuth.getInstance().currentUser!!.uid,
            name,
            phoneNumber,
            bitmapToByteArray(userImage!!),
            UsersRepositoryImpl()
        )
        emit(state)
    }.flowOn(Dispatchers.IO)

    fun setUserImage(context: Context, img: Uri) {
        userImage = makeImageSquare(context, img)
    }

    fun getUserImage(): Bitmap? {
        return userImage
    }

    private fun makeImageSquare(context: Context, img: Uri): Bitmap? {
        val originalBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, img)
        val size = originalBitmap.width.coerceAtMost(originalBitmap.height)
        val x = (originalBitmap.width - size) / 2
        val y = (originalBitmap.height - size) / 2
        return Bitmap.createBitmap(originalBitmap, x, y, size, size)
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}

enum class RegistrationState {
    SUCCESS,
    ERROR_USER_ALREADY_EXISTS,
    ERROR_WEAK_PASSWORD,
    ERROR_INVALID_CREDENTIALS,
    ERROR
}
package com.example.wakemeup.ui.join_room_dialog

import androidx.lifecycle.ViewModel
import com.example.wakemeup.data.RoomsRepositoryImpl
import com.example.wakemeup.domain.AddNewUserToRoomState
import com.example.wakemeup.domain.AddNewUserToRoomUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class JoinRoomDialogViewModel : ViewModel() {
    fun joinRoom(id: String): Flow<AddNewUserToRoomState> = flow {
        emit(AddNewUserToRoomState.LOADING)
        val uid = FirebaseAuth.getInstance().uid
        val state = AddNewUserToRoomUseCase.execute(
            id,
            uid ?: return@flow emit(AddNewUserToRoomState.ERROR),
            RoomsRepositoryImpl()
        )
        emit(state)
    }
}
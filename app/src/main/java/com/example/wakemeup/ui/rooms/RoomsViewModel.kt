package com.example.wakemeup.ui.rooms

import androidx.lifecycle.ViewModel
import com.example.wakemeup.data.RoomsRepositoryImpl
import com.example.wakemeup.domain.GetRoomsUseCase
import com.example.wakemeup.domain.RoomModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class RoomsViewModel : ViewModel() {

    private val _rooms: MutableStateFlow<List<RoomModel>> = MutableStateFlow(emptyList())
    val rooms: Flow<List<RoomModel>> = _rooms

    fun getRooms(uid: String): Flow<Boolean> = flow {
        emit(false)
        _rooms.value = GetRoomsUseCase.execute(uid, RoomsRepositoryImpl())
        emit(true)
    }

}
package com.example.wakemeup.ui.rooms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wakemeup.data.RoomsRepositoryImpl
import com.example.wakemeup.domain.CreateRoomUseCase
import com.example.wakemeup.domain.GetRoomsUseCase
import com.example.wakemeup.domain.RoomModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class RoomsViewModel : ViewModel() {

    private val _rooms: MutableStateFlow<List<RoomModel>> = MutableStateFlow(emptyList())
    val rooms: Flow<List<RoomModel>> = _rooms

    suspend fun getRooms(uid: String) {
        _rooms.value = GetRoomsUseCase.execute(uid, RoomsRepositoryImpl())
    }

}
package com.example.wakemeup.ui.room

import androidx.lifecycle.ViewModel
import com.example.wakemeup.data.RoomsRepositoryImpl
import com.example.wakemeup.domain.FriendModel
import com.example.wakemeup.domain.GetMembersUseCase
import com.example.wakemeup.domain.GetOneRoomUseCase
import com.example.wakemeup.domain.RoomModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoomViewModel : ViewModel() {

    fun getRoom(roomId: String) : Flow<RoomModel?> = flow {
        val repo = RoomsRepositoryImpl()
        emit(GetOneRoomUseCase.execute(roomId, repo))
    }


    fun getMembers(roomId: String) : Flow<List<FriendModel>> = flow {
        val repo = RoomsRepositoryImpl()
        emit(GetMembersUseCase.execute(roomId, repo))
    }

}
package com.example.wakemeup.domain

class GetOneRoomUseCase {
    companion object {
        suspend fun execute(roomId: String, repository: RoomsRepository): RoomModel? {
            return repository.getRoom(roomId)
        }
    }
}
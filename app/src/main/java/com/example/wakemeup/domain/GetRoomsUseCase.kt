package com.example.wakemeup.domain

class GetRoomsUseCase {
    companion object {
        suspend fun execute(uid: String, repository: RoomsRepository): List<RoomModel> {
            return repository.getRooms(uid)
        }
    }
}
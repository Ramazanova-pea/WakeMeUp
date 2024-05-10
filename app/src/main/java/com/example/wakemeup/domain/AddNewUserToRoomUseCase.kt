package com.example.wakemeup.domain

class AddNewUserToRoomUseCase {
    companion object {
        suspend fun execute(roomId: String, userId: String, roomRepository: RoomsRepository): AddNewUserToRoomState {
            val room = roomRepository.getRoom(roomId)
            if (room == null) {
                return AddNewUserToRoomState.ROOM_NOT_FOUND
            }
            if (room.members.contains(userId)) {
                return AddNewUserToRoomState.USER_ALREADY_IN_ROOM
            }
            if (room.isPrivate) {
                return AddNewUserToRoomState.ROOM_IS_PRIVATE
            }
            roomRepository.joinRoom(roomId, userId)
            return AddNewUserToRoomState.SUCCESS
        }
    }
}

enum class AddNewUserToRoomState {
    SUCCESS,
    ROOM_NOT_FOUND,
    ROOM_IS_PRIVATE,
    USER_ALREADY_IN_ROOM,
    LOADING,
    ERROR
}

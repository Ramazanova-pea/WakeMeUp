package com.example.wakemeup.domain


/**
 * Interface for the RoomsRepository.
 */
interface RoomsRepository {

    suspend fun addRoom(roomName: String, roomId: String, isPrivate: Boolean, image: ByteArray): CreateRoomState
    suspend fun getRooms(uid: String): List<RoomModel>
    suspend fun getRoom(roomId: String): RoomModel?
    suspend fun joinRoom(roomId: String, uid: String): Boolean
}
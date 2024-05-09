package com.example.wakemeup.domain

interface RoomsRepository {
    suspend fun addRoom(roomName: String, roomId: String, image: ByteArray): CreateRoomState
    suspend fun getRooms(uid: String): List<RoomModel>
    suspend fun getRoom(roomId: String): RoomModel?
}
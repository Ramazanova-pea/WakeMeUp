package com.example.wakemeup.domain

import android.graphics.Bitmap
import com.example.wakemeup.toByteArray
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class CreateRoomUseCase {
    companion object {
        suspend fun execute(
            roomName: String,
            roomId: String,
            isPrivate: Boolean,
            roomImage: Bitmap,
            repository: RoomsRepository
        ): CreateRoomState {
            repository.getRoom(roomId)?.let {
                return CreateRoomState.ROOM_ALREADY_EXISTS
            }

            repository.addRoom(roomName, roomId, isPrivate, roomImage.toByteArray())

            return CreateRoomState.SUCCESS
        }
    }
}
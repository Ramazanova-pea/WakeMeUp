package com.example.wakemeup.ui.new_room

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import com.example.wakemeup.data.RoomsRepositoryImpl
import com.example.wakemeup.domain.CreateRoomState
import com.example.wakemeup.domain.CreateRoomUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AddNewRoomBSViewModel : ViewModel() {

    private var roomImage: Bitmap? = null
    fun addRoom(roomName: String, roomId: String, isPrivate: Boolean): Flow<CreateRoomState> = flow {
        emit(CreateRoomState.LOADING)
        val state = CreateRoomUseCase.execute(
            roomName = roomName,
            roomId = roomId,
            roomImage = roomImage!!,
            isPrivate = isPrivate,
            repository = RoomsRepositoryImpl()
        )
        emit(state)
    }.flowOn(Dispatchers.IO)

    fun setRoomImage(context: Context, img: Uri) {
        roomImage = makeImageSquare(context, img)
    }

    fun getRoomImage(): Bitmap? {
        return roomImage
    }

    private fun makeImageSquare(context: Context, img: Uri): Bitmap? {
        val originalBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, img)
        val size = originalBitmap.width.coerceAtMost(originalBitmap.height)
        val x = (originalBitmap.width - size) / 2
        val y = (originalBitmap.height - size) / 2
        return Bitmap.createBitmap(originalBitmap, x, y, size, size)
    }

}



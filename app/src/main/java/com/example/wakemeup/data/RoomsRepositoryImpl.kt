package com.example.wakemeup.data

import android.util.Log
import com.example.wakemeup.domain.CreateRoomState
import com.example.wakemeup.domain.RoomModel
import com.example.wakemeup.domain.RoomsRepository
import com.example.wakemeup.domain.UserModel
import com.example.wakemeup.domain.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

data class Room(
    val roomName: String = "",
    val roomId: String = "",
    val imageUrl: String = "",
    val members: MutableList<String> = mutableListOf(),  // User IDs
    val administrators: MutableList<String> = mutableListOf(),  // User IDs
    val notes: MutableMap<String, String> = mutableMapOf()  // User ID -> Note
)

class RoomsRepositoryImpl : RoomsRepository {
    override suspend fun addRoom(
        roomName: String,
        roomId: String,
        image: ByteArray
    ): CreateRoomState {
        val storageRef = FirebaseStorage.getInstance().reference.child("images/$roomId.jpg")
        val uploadTask = storageRef.putBytes(image)
        uploadTask.await()

        val imageUrl = storageRef.downloadUrl.await().toString()

        val room = Room(
            roomName,
            roomId,
            imageUrl
        )

        room.members.add(FirebaseAuth.getInstance().uid!!)
        room.administrators.add(FirebaseAuth.getInstance().uid!!)

        val user = UsersRepositoryImpl().getUser(FirebaseAuth.getInstance().uid!!) ?: return CreateRoomState.ERROR
        user.rooms.add(roomId)
        UsersRepositoryImpl().updateUser(
            user.uid,
            user
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("rooms").document(roomId).set(room).await()

        return CreateRoomState.SUCCESS
    }

    override suspend fun getRooms(uid: String): List<RoomModel> {
        val user = UsersRepositoryImpl().getUser(uid) ?: return emptyList()
        val rooms = mutableListOf<RoomModel>()

        for (roomId in user.rooms) {
            val room = getRoom(roomId)
            if (room != null) {
                rooms.add(room)
            }
        }

        return rooms
    }

    override suspend fun getRoom(roomId: String): RoomModel? {
        return try {
            val db = FirebaseFirestore.getInstance()
            val doc = db.collection("rooms").document(roomId).get().await()
            val room = doc.toObject(Room::class.java)
            val storageRef = FirebaseStorage.getInstance().reference.child("images/$roomId.jpg")
            val image = storageRef.getBytes(1024 * 1024).await()

            RoomModel(
                room!!.roomName,
                room.roomId,
                image,
                room.members,
                room.administrators,
                room.notes
            )
        } catch (e: Exception) {
            Log.d("RoomsRepositoryImpl", "Room not found")
            null
        }


    }
}
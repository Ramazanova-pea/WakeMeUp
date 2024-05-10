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

/**
 * Data class representing a Room.
 * @param roomName The name of the room.
 * @param roomId The ID of the room.
 * @param isPrivate Whether the room is private.
 * @param blockedUsers The list of blocked users.
 * @param members The list of members.
 * @param administrators The list of administrators.
 * @param notes The map of notes.
 */
data class Room(
    val roomName: String = "",
    val roomId: String = "",
    val private: Boolean = false,
    val blockedUsers: MutableList<String> = mutableListOf(),  // User IDs
    val members: MutableList<String> = mutableListOf(),  // User IDs
    val administrators: MutableList<String> = mutableListOf(),  // User IDs
    val notes: MutableMap<String, String> = mutableMapOf()  // User ID -> Note
)

/**
 * Implementation of the RoomsRepository interface.
 * @see RoomsRepository
 */
class RoomsRepositoryImpl : RoomsRepository {

    /**
     * Adds a room to the Firebase database.
     * @param roomName The name of the room.
     * @param roomId The ID of the room.
     * @param isPrivate Whether the room is private.
     * @param image The image of the room.
     * @return A CreateRoomState representing the state of the room creation.
     * @see CreateRoomState
     * @see Room
     */
    override suspend fun addRoom(
        roomName: String,
        roomId: String,
        isPrivate: Boolean,
        image: ByteArray
    ): CreateRoomState {

        // Upload image
        val storageRef = FirebaseStorage.getInstance().reference.child("images/$roomId.jpg")
        val uploadTask = storageRef.putBytes(image)
        uploadTask.await()

        // Create room
        val room = Room(
            roomName,
            roomId,
            isPrivate
        )

        // Add user to room
        room.members.add(FirebaseAuth.getInstance().uid!!)
        room.administrators.add(FirebaseAuth.getInstance().uid!!)

        // Add room to user
        val user = UsersRepositoryImpl().getUser(FirebaseAuth.getInstance().uid!!) ?: return CreateRoomState.ERROR
        user.rooms.add(roomId)
        UsersRepositoryImpl().updateUser(
            user.uid,
            user
        )

        // Add room to database
        val db = FirebaseFirestore.getInstance()
        db.collection("rooms").document(roomId).set(room).await()

        return CreateRoomState.SUCCESS
    }

    /**
     * Gets the rooms of a user.
     * @param uid The ID of the user.
     * @return A list of RoomModel representing the rooms of the user.
     * @see RoomModel
     */
    override suspend fun getRooms(uid: String): List<RoomModel> {
        // Get user
        val user = UsersRepositoryImpl().getUser(uid) ?: return emptyList()
        val rooms = mutableListOf<RoomModel>()

        // Get rooms
        for (roomId in user.rooms) {
            val room = getRoom(roomId)
            if (room != null) {
                rooms.add(room)
            }
        }

        return rooms
    }

    /**
     * Gets a room.
     * @param roomId The ID of the room.
     * @return A RoomModel representing the room.
     * @see RoomModel
     */
    override suspend fun getRoom(roomId: String): RoomModel? {
        return try {
            val db = FirebaseFirestore.getInstance()
            val doc = db.collection("rooms").document(roomId).get().await()
            val room = doc.toObject(Room::class.java)
            roomToRoomModel(room!!)
        } catch (e: Exception) {
            Log.d("RoomsRepositoryImpl", "Room not found")
            null
        }
    }

    /**
     * Joins a room.
     * @param roomId The ID of the room.
     * @param uid The ID of the user.
     * @return A boolean representing whether the user joined the room.
     */
    override suspend fun joinRoom(roomId: String, uid: String): Boolean {
        val room = roomModelToRoom(getRoom(roomId)?: return false)
        room.members.add(uid)

        // Remove duplicates from members
        val members = room.members.toSet().toMutableList()
        room.members.clear()
        room.members.addAll(members)

        // Remove duplicates from administrators
        val administrators = room.administrators.toSet().toMutableList()
        room.administrators.clear()
        room.administrators.addAll(administrators)

        // Add room to user
        val user = UsersRepositoryImpl().getUser(uid) ?: return false
        user.rooms.add(roomId)
        UsersRepositoryImpl().updateUser(uid, user)

        // Update room
        val db = FirebaseFirestore.getInstance()
        db.collection("rooms").document(roomId).set(room).await()
        return true
    }

    /**
     * Converts a Room to a RoomModel.
     * @param room The Room to convert.
     * @return A RoomModel representing the Room.
     * @see RoomModel
     */
    private suspend fun roomToRoomModel(room: Room): RoomModel {
        // Get image
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${room.roomId}.jpg")
        val image = storageRef.getBytes(16 * 1024 * 1024).await() // 16 MB limit for image

        return RoomModel(
            room.roomName,
            room.roomId,
            image,
            room.private,
            room.blockedUsers,
            room.members,
            room.administrators,
            room.notes
        )
    }

    fun roomModelToRoom(roomModel: RoomModel): Room {
        return Room(
            roomModel.roomName,
            roomModel.roomId,
            roomModel.isPrivate,
            roomModel.blockedUsers,
            roomModel.members,
            roomModel.administrators,
            roomModel.notes
        )
    }
}
package com.example.wakemeup.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.wakemeup.domain.FriendModel
import com.example.wakemeup.domain.UserModel
import com.example.wakemeup.domain.UsersRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.Calendar

/**
 * Data class representing a User.
 * @param uid The ID of the user.
 * @param name The name of the user.
 * @param phoneNumber The phone number of the user.
 * @param profilePicUrl The URL of the user's profile picture.
 * @param isAwake Whether the user is awake.
 * @param timeToAwake The time the user is set to wake up.
 * @param rooms The list of rooms the user is in.
 */
data class User(
    val uid: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val isAwake: Boolean = false,
    val timeToAwake: Timestamp = Timestamp.now(),
    val rooms: MutableList<String> = mutableListOf()  // rooms IDs
)

/**
 * Implementation of the UsersRepository interface.
 * @see UsersRepository
 */
class UsersRepositoryImpl : UsersRepository {

    /**
     * Gets a user from the Firebase database.
     * @param uid The ID of the user.
     * @return A UserModel representing the user.
     * @see UserModel
     * @see User
     */
    override suspend fun getUser(uid: String): UserModel? {
        // Get user from Firestore
        return try {
            // Get user from Firestore
            // It is important to have a non-argument constructor in the data class
            val db = FirebaseFirestore.getInstance()
            val doc = db.collection("users").document(uid).get().await()
            val user = doc.toObject(User::class.java)!!

            // Get profile pic from Firebase Storage
            val storageRef = FirebaseStorage.getInstance().reference.child("profilePics/${user.uid}.jpg")
            val profilePic = storageRef.getBytes(16 * 1024 * 1024).await()  // 16 MB limit for image

            // Return UserModel
            UserModel(
                user.uid,
                user.name,
                user.phoneNumber,
                profilePic,
                user.isAwake,
                user.timeToAwake.seconds,
                user.rooms
            )
        } catch (e: Exception) {
            // Log error and return null
            Log.e("UsersRepositoryImpl", "Error getting user", e)
            null
        }
    }

    /**
     * Updates a user in the Firebase database.
     * @param uid The ID of the user.
     * @param user The UserModel representing the user.
     * @see UserModel
     * @see User
     */
    override suspend fun updateUser(uid: String, user: UserModel) {
        // Upload profile pic to Firebase Storage
        val storageRef = FirebaseStorage.getInstance().reference.child("profilePics/$uid.jpg")
        val uploadTask = storageRef.putBytes(user.profilePic)
        uploadTask.await()

        // Remove duplicates from rooms
        val rooms = user.rooms.toMutableSet().toList()
        user.rooms.clear()
        user.rooms.addAll(rooms)

        // Update user in Firestore
        val user = User(
            user.uid,
            user.name,
            user.phoneNumber,
            user.isAwake,
            Timestamp(user.timeToAwake, 0),
            user.rooms
        )
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(user.uid).set(user).await()
    }

    /**
     * Creates a user in the Firebase database.
     * @param user The UserModel representing the user.
     * @see UserModel
     * @see User
     */
    override suspend fun createUser(user: UserModel) {
        // Upload profile pic to Firebase Storage
        val storageRef = FirebaseStorage.getInstance().reference.child("profilePics/${user.uid}.jpg")
        val uploadTask = storageRef.putBytes(user.profilePic)
        uploadTask.await()

        // Create user in Firestore
        val user = User(
            user.uid,
            user.name,
            user.phoneNumber,
            user.isAwake,
            Timestamp(user.timeToAwake, 0)
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(user.uid).set(user).await()
    }

    /**
     * Gets the friends list of a user.
     * @return A list of FriendModel representing the friends of the user.
     * @see FriendModel
     */
    override suspend fun getFriendsList(): ArrayList<FriendModel> {

        val thisUser = getUser(FirebaseAuth.getInstance().uid!!) ?: return ArrayList() // get the current user
        val friends = mutableSetOf<UserModel>()  // set to avoid duplicates

        // get all the friends of the user
        thisUser.rooms.forEach { roomId ->
            val room = RoomsRepositoryImpl().getRoom(roomId) ?: return@forEach  // get the room by id or return if null
            room.members.forEach { memberId ->  // get all the members of the room
                if (memberId != thisUser.uid) {  // if the member is not the current user
                    val friend = getUser(memberId) ?: return@forEach  // get the user by id or return if null
                    friends.add(friend)
                }
            }
        }

        // convert the users to FriendModel
        return ArrayList(friends.map {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it.timeToAwake
            FriendModel(
                it.name,
                it.phoneNumber,
                it.isAwake,
                calendar,
                bytearrayToBitmap(it.profilePic.toList())
            )
        })
    }

    /**
     * Converts a list of bytes to a Bitmap.
     * @param byteList The list of bytes.
     * @return The Bitmap.
     */
    private fun bytearrayToBitmap(byteList: List<Byte>): Bitmap {
        val byteArray = byteList.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}
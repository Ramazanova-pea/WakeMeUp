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

data class User(
    val uid: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val profilePicUrl: String = "",
    val isAwake: Boolean = false,
    val timeToAwake: Timestamp = Timestamp.now(),
    val rooms: MutableList<String> = mutableListOf()  // rooms IDs
)

class UsersRepositoryImpl : UsersRepository {
    override suspend fun getUser(uid: String): UserModel? {
        return try {
            val db = FirebaseFirestore.getInstance()
            val doc = db.collection("users").document(uid).get().await()
            val user = doc.toObject(User::class.java)!!
            val storageRef = FirebaseStorage.getInstance().reference.child("profilePics/${user.uid}.jpg")
            val profilePic = storageRef.getBytes(1024 * 1024).await()
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
            Log.e("UsersRepositoryImpl", "Error getting user", e)
            null
        }
    }

    override suspend fun updateUser(uid: String, user: UserModel) {
        val storageRef = FirebaseStorage.getInstance().reference.child("profilePics/$uid.jpg")
        val uploadTask = storageRef.putBytes(user.profilePic)
        uploadTask.await()
        val profilePicUrl = storageRef.downloadUrl.await().toString()

        val user = User(
            user.uid,
            user.name,
            user.phoneNumber,
            profilePicUrl,
            user.isAwake,
            Timestamp(user.timeToAwake, 0),
            user.rooms
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(user.uid).set(user).await()
    }

    override suspend fun createUser(user: UserModel) {
        val storageRef = FirebaseStorage.getInstance().reference.child("profilePics/${user.uid}.jpg")
        val uploadTask = storageRef.putBytes(user.profilePic)
        uploadTask.await()
        val profilePicUrl = storageRef.downloadUrl.await().toString()

        val user = User(
            user.uid,
            user.name,
            user.phoneNumber,
            profilePicUrl,
            user.isAwake,
            Timestamp(user.timeToAwake, 0)
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(user.uid).set(user).await()
    }

    override suspend fun getFriendsList(): ArrayList<FriendModel> {
        val thisUser = getUser(FirebaseAuth.getInstance().uid!!) ?: return ArrayList()
        val friends = mutableSetOf<UserModel>()
        thisUser.rooms.forEach { roomId ->
            val room = RoomsRepositoryImpl().getRoom(roomId) ?: return@forEach
            room.members.forEach { memberId ->
                if (memberId != thisUser.uid) {
                    val friend = getUser(memberId) ?: return@forEach
                    friends.add(friend)
                }
            }
        }
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

    private fun bytearrayToBitmap(byteList: List<Byte>): Bitmap {
        val byteArray = byteList.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}
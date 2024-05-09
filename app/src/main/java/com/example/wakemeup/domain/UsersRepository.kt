package com.example.wakemeup.domain

interface UsersRepository {
    suspend fun getUser(uid: String): UserModel?
    suspend fun updateUser(uid: String, user: UserModel)
    suspend fun createUser(user: UserModel)
    suspend fun getFriendsList(): List<FriendModel>
}
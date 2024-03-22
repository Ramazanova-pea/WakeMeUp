package com.example.wakemeup.domain

interface FriendsRepository {
    fun getFriendsList(): ArrayList<FriendModel>
}
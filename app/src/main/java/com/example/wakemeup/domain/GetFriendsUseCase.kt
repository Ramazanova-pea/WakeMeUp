package com.example.wakemeup.domain

class GetFriendsUseCase {
    companion object {
        suspend fun execute(repository: UsersRepository): List<FriendModel> {
            return repository.getFriendsList()
        }
    }
}
package com.example.wakemeup.domain

class GetMembersUseCase {
    companion object {
        suspend fun execute(roomId: String, repo: RoomsRepository): List<FriendModel> {
            return repo.getMembers(roomId)
        }
    }
}
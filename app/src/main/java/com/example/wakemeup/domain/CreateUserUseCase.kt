package com.example.wakemeup.domain

class CreateUserUseCase {
    companion object {
        suspend fun execute(
            uid: String,
            name: String,
            phoneNumber: String,
            profilePic: ByteArray,
            usersRepository: UsersRepository) {
            val user = UserModel(
                uid = uid,
                name = name,
                phoneNumber = phoneNumber,
                profilePic = profilePic,
                isAwake = false,
                timeToAwake = 0
            )
            usersRepository.createUser(user)
        }
    }
}
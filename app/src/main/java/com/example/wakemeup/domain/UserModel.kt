package com.example.wakemeup.domain

data class UserModel(
    val uid: String,
    val name: String,
    val phoneNumber: String,
    val profilePic: ByteArray,
    val isAwake: Boolean,
    val timeToAwake: Long,
    val rooms: MutableList<String> = mutableListOf()  // rooms IDs
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserModel

        if (uid != other.uid) return false
        if (name != other.name) return false
        if (phoneNumber != other.phoneNumber) return false
        if (!profilePic.contentEquals(other.profilePic)) return false
        if (isAwake != other.isAwake) return false
        if (timeToAwake != other.timeToAwake) return false
        if (rooms != other.rooms) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        result = 31 * result + profilePic.contentHashCode()
        result = 31 * result + isAwake.hashCode()
        result = 31 * result + timeToAwake.hashCode()
        result = 31 * result + rooms.hashCode()
        return result
    }
}

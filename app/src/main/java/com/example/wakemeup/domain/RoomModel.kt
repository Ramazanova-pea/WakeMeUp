package com.example.wakemeup.domain

data class RoomModel(
    val roomName: String,
    val roomId: String,
    val image: ByteArray,
    val isPrivate: Boolean = false,
    val blockedUsers: MutableList<String> = mutableListOf(),  // User IDs
    val members: MutableList<String> = mutableListOf(),  // User IDs
    val administrators: MutableList<String> = mutableListOf(),  // User IDs
    val notes: MutableMap<String, String> = mutableMapOf()  // User ID -> Note
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoomModel

        if (roomName != other.roomName) return false
        if (roomId != other.roomId) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = roomName.hashCode()
        result = 31 * result + roomId.hashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }
}

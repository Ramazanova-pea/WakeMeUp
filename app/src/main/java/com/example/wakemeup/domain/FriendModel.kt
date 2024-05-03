package com.example.wakemeup.domain

import android.graphics.drawable.Drawable
import java.util.Calendar

/**
 * Data class representing a friend in the application.
 *
 * @property name The name of the friend.
 * @property phoneNumber The phone number of the friend.
 * @property profilePic The profile picture of the friend.
 * @property isAwake The awake status of the friend.
 * @property notes Any additional notes for the friend.
 * @property wakingTime The time the friend usually wakes up.
 */
data class FriendModel (
    var name: String,
    var phoneNumber: String,
    var profilePic: Drawable,
    var isAwake: Boolean,
    var notes: String?,
    var wakingTime: Calendar
)
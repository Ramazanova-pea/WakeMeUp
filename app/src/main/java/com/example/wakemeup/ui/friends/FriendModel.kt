package com.example.wakemeup.ui.friends

import android.graphics.drawable.Drawable
import java.util.Calendar

data class FriendModel (
    var name: String,
    var phoneNumber: String,
    var profilePic: Drawable,
    var isAwake: Boolean,
    var notes: String?,
    var wakingTime: Calendar
    )
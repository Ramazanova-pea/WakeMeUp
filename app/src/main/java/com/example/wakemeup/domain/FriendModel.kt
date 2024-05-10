package com.example.wakemeup.domain

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import java.util.Calendar

//
//FriendModel(
//name="Daniel",
//phoneNumber="1234567890",
//isAwake=true,
//wakingTime= Calendar.getInstance(),
//profilePic= resources.getDrawable(R.drawable.ic_launcher_foreground),
//notes="This is a note"
//)
data class FriendModel(
    val name: String,
    val phoneNumber: String,
    val isAwake: Boolean,
    val wakingTime: Calendar,
    val profilePic: Bitmap
)

//package com.example.wakemeup.data
//
//import android.graphics.drawable.Drawable
//import com.example.wakemeup.R
//import com.example.wakemeup.domain.FriendModel
//import com.example.wakemeup.domain.FriendsRepository
//
//import java.util.Calendar
//
//class FriendsRepositoryImpl : FriendsRepository {
//    override fun getFriendsList(): ArrayList<FriendModel> {
//        return arrayListOf(
//            FriendModel(
//                name = "Daniel",
//                phoneNumber = "1234567890",
//                isAwake = true,
//                wakingTime = Calendar.getInstance(),
//                profilePic = Drawable.createFromPath("R.drawable.ic_launcher_foreground")!!,
//                notes = "This is a note"
//            ),
//            FriendModel(
//                name = "Peter",
//                phoneNumber = "1234567890",
//                isAwake = false,
//                wakingTime = Calendar.getInstance(),
//                profilePic = resources.getDrawable(R.drawable.ic_launcher_foreground),
//                notes = "This is a note"
//            ),
//            FriendModel(
//                name = "Marina",
//                phoneNumber = "1234567890",
//                isAwake = true,
//                wakingTime = Calendar.getInstance(),
//                profilePic = resources.getDrawable(R.drawable.ic_launcher_foreground),
//                notes = "This is a note"
//            ),
//            FriendModel(
//                name = "hhhhhhhhhhhhhhhhh",
//                phoneNumber = "1234567890",
//                isAwake = true,
//                wakingTime = Calendar.getInstance(),
//                profilePic = resources.getDrawable(R.drawable.ic_launcher_foreground),
//                notes = "This is a note"
//            ),
//        )
//    }
//}
package com.example.wakemeup.ui.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wakemeup.data.UsersRepositoryImpl
import com.example.wakemeup.domain.FriendModel
import com.example.wakemeup.domain.GetFriendsUseCase
import com.example.wakemeup.isNetworkAvailable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class FriendsViewModel : ViewModel() {

    private val _friends = MutableStateFlow(emptyList<FriendModel>())
    val friends: Flow<List<FriendModel>> = _friends

    suspend fun getFriends(): Flow<Boolean> = flow {
        emit(false)
        _friends.value = GetFriendsUseCase.execute(UsersRepositoryImpl())
        emit(true)
    }
}
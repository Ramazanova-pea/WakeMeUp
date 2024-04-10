package com.example.wakemeup.domain

interface AccountReository {
    fun signUp(signUpModel: SignUpModel): Boolean
    fun login(loginModel: LoginModel): Boolean
    fun logout(): Boolean
    fun isAuthorized(): Boolean
    fun getCurrentUser(): UserModel
    fun saveUser(userModel: UserModel)
    fun clearUser()
}
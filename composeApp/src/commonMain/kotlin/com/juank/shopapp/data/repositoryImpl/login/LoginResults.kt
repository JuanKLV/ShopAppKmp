package com.juank.shopapp.data.repositoryImpl.login

import com.juank.shopapp.data.mappers.dto.UserDto

sealed class LoginResults {
    data class IsCorrect(val isAuthenticated: Boolean) : LoginResults()
    data class CurrentUser(val user: UserDto): LoginResults()
    data class Error(val message: String) : LoginResults()
}

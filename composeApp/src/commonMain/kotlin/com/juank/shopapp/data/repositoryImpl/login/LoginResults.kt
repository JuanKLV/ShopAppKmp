package com.juank.shopapp.data.repositoryImpl.login

import com.juank.shopapp.data.mappers.dto.UserDto

sealed class LoginResults {
    data class IsCorrect(val isLogged: Boolean) : LoginResults()
    data class CurrentUser(val user: UserDto): LoginResults()
}

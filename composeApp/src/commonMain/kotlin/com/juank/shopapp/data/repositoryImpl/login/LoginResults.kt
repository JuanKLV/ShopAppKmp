package com.juank.shopapp.data.repositoryImpl.login

sealed class LoginResults {
    data class IsCorrect(val isLogged: Boolean) : LoginResults()
}

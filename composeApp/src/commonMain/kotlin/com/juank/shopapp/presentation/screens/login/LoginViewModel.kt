package com.juank.shopapp.presentation.screens.login

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.juank.shopapp.data.mappers.dto.UserDto
import com.juank.shopapp.data.repositoryImpl.login.LoginResults
import com.juank.shopapp.domain.useCase.login.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginScreenUiState(
    val email: String = "",
    val password: String = "",
    val visiblePassword: Boolean = false,
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
    val currentUser: UserDto? = null
)

class LoginViewModel(private val loginUseCase: LoginUseCase): ScreenModel {
    private var _uiState = MutableStateFlow(LoginScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        screenModelScope.launch {
            loginUseCase.getCurrentUser().collect { value ->
                when (value) {
                    is LoginResults.CurrentUser -> {
                        _uiState.update {
                            it.copy(currentUser = value.user)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun setEmail(value: String) {
        _uiState.update {
            it.copy(email = value, emailError = false)
        }
    }

    fun setPassword(value: String) {
        _uiState.update {
            it.copy(password = value, passwordError = false)
        }
    }

    fun setVisiblePassword(value: Boolean) {
        _uiState.update {
            it.copy(visiblePassword = value)
        }
    }

    fun signIn() {
        if (_uiState.value.email.isEmpty() && _uiState.value.password.isEmpty()) {
            _uiState.update {
                it.copy(
                    emailError = true,
                    passwordError = true
                )
            }
            return
        }

        if (_uiState.value.email.isEmpty()) {
            _uiState.update {
                it.copy(emailError = true)
            }
            return
        }

        if (_uiState.value.password.isEmpty()) {
            _uiState.update {
                it.copy(passwordError = true)
            }
            return
        }

        screenModelScope.launch {
            loginUseCase.authenticate(_uiState.value.email, _uiState.value.password).collect {

            }
        }
    }

    fun signOut() {
        screenModelScope.launch {
            loginUseCase.signOut().collect {

            }
        }
    }
}
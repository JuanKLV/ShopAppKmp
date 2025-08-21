package com.juank.shopapp.presentation.screens.login

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.juank.shopapp.data.local.database.tables.categories.Categories
import com.juank.shopapp.data.local.database.tables.products.Products
import com.juank.shopapp.data.mappers.dto.UserDto
import com.juank.shopapp.data.repositoryImpl.login.LoginResults
import com.juank.shopapp.domain.useCase.login.LoginUseCase
import com.juank.shopapp.domain.utils.AppDao
import com.juank.shopapp.presentation.screens.routes.Routes.PRINCIPAL_SCREEN
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
    val currentUser: UserDto? = null,
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val isErrorAuthenticated: Boolean = false,
    val errorMessage: String = ""
)

class LoginViewModel(private val loginUseCase: LoginUseCase, private val appDao: AppDao): ScreenModel {
    private var _uiState = MutableStateFlow(LoginScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        screenModelScope.launch {
            preloadData()
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

    fun clearErrorMessage() {
        _uiState.update {
            it.copy(isErrorAuthenticated = false, errorMessage = "")
        }
    }

    fun signIn(navigator: Navigator?) {
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
            _uiState.update { it.copy(isLoading = true) }

            loginUseCase.authenticate(_uiState.value.email, _uiState.value.password).collect { result ->
                when (result) {
                    is LoginResults.IsCorrect -> {
                        _uiState.update {
                            it.copy(isAuthenticated = result.isAuthenticated, isLoading = false)
                        }
                        navigator?.replaceAll(PRINCIPAL_SCREEN)
                    }
                    is LoginResults.Error -> {
                        if (result.message.contains("Usuario no registrado.")) {
                            try {
                                loginUseCase.createUser(_uiState.value.email, _uiState.value.password).collect { createUser ->
                                    when (createUser) {
                                        is LoginResults.IsCorrect -> {
                                            _uiState.update {
                                                it.copy(isAuthenticated = createUser.isAuthenticated, isLoading = false)
                                            }
                                        }
                                        is LoginResults.Error -> {
                                            _uiState.update {
                                                it.copy(errorMessage = createUser.message, isLoading = false, isErrorAuthenticated = true)
                                            }
                                        }
                                        else -> {}
                                    }
                                }
                            } catch (e: Exception) {
                                _uiState.update {
                                    it.copy(
                                        errorMessage = "Error creando el usuario: ${e.message}",
                                        isErrorAuthenticated = true,
                                        isLoading = false
                                    )
                                }
                            }
                        } else {
                            _uiState.update {
                                it.copy(
                                    errorMessage = result.message,
                                    isErrorAuthenticated = true,
                                    isLoading = false
                                )
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private suspend fun preloadData() {
        if (appDao.categoriesDao.countCategories() == 0) {
            appDao.categoriesDao.saveCategories(
                listOf(
                    Categories(categoryName = "Todos"),
                    Categories(categoryName = "Ropa"),
                    Categories(categoryName = "Accesorios"),
                    Categories(categoryName = "Zapatos")
                )
            )
        }

        if (appDao.productsDao.countProducts() == 0) {
            appDao.productsDao.saveProducts(
                listOf(
                    Products(productName = "Camiseta de algodón", price = 25000.0, image = "camiseta.png", categoryId = 2),
                    Products(productName = "Gorra deportiva", price = 150000.0, image = "gorra.png", categoryId = 3),
                    Products(productName = "Zapatillas urbanas", price = 80000.0, image = "zapatillas.png", categoryId = 4),
                    Products(productName = "Bolso de mano", price = 45000.0, image = "bolso.png", categoryId = 3)
                )
            )
        }
    }
}
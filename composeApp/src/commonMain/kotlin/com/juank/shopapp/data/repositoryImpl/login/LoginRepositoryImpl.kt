package com.juank.shopapp.data.repositoryImpl.login

import com.juank.shopapp.data.remote.services.auth.AuthException
import com.juank.shopapp.data.remote.services.auth.AuthService
import com.juank.shopapp.domain.repository.login.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginRepositoryImpl(private val authService: AuthService): LoginRepository {
    override suspend fun authenticate(email: String, password: String): Flow<LoginResults> {
        return flow {
            try {
                authService.authenticate(email, password)
                val isAuthenticated = authService.isAuthenticated
                emit(LoginResults.IsCorrect(isAuthenticated = isAuthenticated))
            } catch (e: AuthException) {
                emit(LoginResults.Error(e.message ?: "Error desconocido."))
            }
        }
    }

    override suspend fun createUser(email: String, password: String): Flow<LoginResults> {
        return flow {
            try {
                authService.createUser(email, password)
                emit(LoginResults.IsCorrect(isAuthenticated = true))
            } catch (e: Exception) {
                emit(LoginResults.Error(e.message ?: "Error al crear usuario."))
            }
        }
    }

    override suspend fun getCurrentUser(): Flow<LoginResults> {
        return flow {
            authService.currentUser.collect {
                emit(LoginResults.CurrentUser(it))
            }
        }
    }
}
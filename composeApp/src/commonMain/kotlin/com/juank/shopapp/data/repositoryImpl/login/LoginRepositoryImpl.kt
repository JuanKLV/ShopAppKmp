package com.juank.shopapp.data.repositoryImpl.login

import com.juank.shopapp.data.remote.services.auth.AuthService
import com.juank.shopapp.domain.repository.login.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginRepositoryImpl(private val authService: AuthService): LoginRepository {
    override suspend fun authenticate(email: String, password: String): Flow<LoginResults> {
        return flow {
            authService.authenticate(email, password)
        }
    }

    override suspend fun getCurrentUser(): Flow<LoginResults> {
        return flow {
            authService.currentUser.collect {
                emit(LoginResults.CurrentUser(it))
            }
        }
    }

    override suspend fun signOut(): Flow<LoginResults> {
        return flow {
            authService.signOut()
        }
    }

}
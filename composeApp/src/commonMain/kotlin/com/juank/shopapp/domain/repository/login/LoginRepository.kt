package com.juank.shopapp.domain.repository.login

import com.juank.shopapp.data.repositoryImpl.login.LoginResults
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun authenticate(email: String, password: String): Flow<LoginResults>

    suspend fun getCurrentUser(): Flow<LoginResults>

    suspend fun signOut() : Flow<LoginResults>
}
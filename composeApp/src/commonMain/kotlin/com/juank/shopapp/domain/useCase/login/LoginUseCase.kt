package com.juank.shopapp.domain.useCase.login

import com.juank.shopapp.data.repositoryImpl.login.LoginResults
import com.juank.shopapp.domain.repository.login.LoginRepository
import kotlinx.coroutines.flow.Flow

class LoginUseCase(private val loginRepository: LoginRepository) {
    suspend fun authenticate(email: String, password: String) : Flow<LoginResults> {
        return loginRepository.authenticate(email, password)
    }

    suspend fun getCurrentUser() : Flow<LoginResults> {
        return loginRepository.getCurrentUser()
    }

    suspend fun signOut() : Flow<LoginResults> {
        return loginRepository.signOut()
    }
}
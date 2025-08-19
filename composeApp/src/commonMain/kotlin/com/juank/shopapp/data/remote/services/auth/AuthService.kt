package com.juank.shopapp.data.remote.services.auth

import com.juank.shopapp.data.mappers.dto.UserDto
import kotlinx.coroutines.flow.Flow

interface AuthService {
    val currentUserId: String
    val isAuthenticated: Boolean
    val currentUser: Flow<UserDto>

    suspend fun authenticate(email: String, password: String)

    suspend fun createUser(email: String, password: String)

    suspend fun signOut()
}
package com.juank.shopapp.data.remote.services.auth

import com.juank.shopapp.data.mappers.dto.UserDto
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthServiceImpl(
    private val auth: FirebaseAuth
) : AuthService {

    override val currentUser: Flow<UserDto>
        get() = auth.authStateChanged.map {
            it?.let {
                UserDto(userId = it.uid, isAnonymous = it.isAnonymous)
            } ?: UserDto() }

    override val currentUserId: String
        get() = auth.currentUser?.uid.toString()

    override val isAuthenticated: Boolean
        get() = auth.currentUser != null && auth.currentUser?.isAnonymous == false


    override suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
    }

    override suspend fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
    }

    override suspend fun signOut() {
        if (auth.currentUser?.isAnonymous == true) {
            auth.currentUser?.delete()
        }

        auth.signOut()
    }
}
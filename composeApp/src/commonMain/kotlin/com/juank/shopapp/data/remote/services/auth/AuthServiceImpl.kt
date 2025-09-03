package com.juank.shopapp.data.remote.services.auth

import com.juank.shopapp.data.mappers.dto.UserDto
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseAuthEmailException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthException(message: String, cause: Throwable? = null) : Exception(message, cause)

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
        try {
            auth.signInWithEmailAndPassword(email, password)
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthInvalidUserException -> {
                    throw AuthException("Usuario no registrado.", e)
                }
                is FirebaseAuthInvalidCredentialsException -> {
                    throw AuthException("Usuario o contraseña incorrecta.", e)
                }
                is FirebaseAuthEmailException -> {
                    throw AuthException("Correo invalido.", e)
                }
                else -> {
                    throw e
                }
            }
        }
    }

    override suspend fun createUser(email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(email, password)
        } catch (e: Exception) {
            throw AuthException("Error al crear el usuario.", e)
        }
    }

    override suspend fun signOut() {
        if (auth.currentUser?.isAnonymous == true) {
            auth.currentUser?.delete()
        }

        try {
            auth.signOut()
        } catch (e: Exception) {
            throw AuthException("Error al desloguear", e)
        }
    }
}
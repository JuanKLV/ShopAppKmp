package com.juank.shopapp.domain.useCase.principal

import com.juank.shopapp.data.mappers.dto.CartItemDto
import com.juank.shopapp.data.repositoryImpl.principal.PrincipalResults
import com.juank.shopapp.domain.repository.principal.PrincipalRepository
import kotlinx.coroutines.flow.Flow

class PrincipalUseCase(private val principalRepository: PrincipalRepository) {
    suspend fun getProducts(): Flow<PrincipalResults> {
        return principalRepository.getProducts()
    }

    suspend fun getCategories(): Flow<PrincipalResults> {
        return principalRepository.getCategories()
    }

    suspend fun signOut(): Flow<PrincipalResults> {
        return principalRepository.signOut()
    }

    suspend fun addToCart(cartItems: CartItemDto): Flow<PrincipalResults> {
        return principalRepository.addToCart(cartItems)
    }

    suspend fun getCartItems(): Flow<PrincipalResults> {
        return principalRepository.getCartItems()
    }

    suspend fun clearCart() : Flow<PrincipalResults> {
        return principalRepository.clearCart()
    }

}
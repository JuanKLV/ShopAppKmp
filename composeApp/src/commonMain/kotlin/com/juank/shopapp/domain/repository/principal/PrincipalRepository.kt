package com.juank.shopapp.domain.repository.principal

import com.juank.shopapp.data.local.database.tables.cartItems.CartItemsTable
import com.juank.shopapp.data.mappers.dto.CartItemDto
import com.juank.shopapp.data.repositoryImpl.principal.PrincipalResults
import kotlinx.coroutines.flow.Flow

interface PrincipalRepository {
    suspend fun getProducts(): Flow<PrincipalResults>
    suspend fun getCategories(): Flow<PrincipalResults>
    suspend fun signOut(): Flow<PrincipalResults>
    suspend fun addToCart(cartItems: CartItemDto) : Flow<PrincipalResults>
    suspend fun getCartItems() : Flow<PrincipalResults>
    suspend fun clearCart(): Flow<PrincipalResults>

}
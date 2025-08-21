package com.juank.shopapp.domain.repository.shoppingCart

import com.juank.shopapp.data.mappers.dto.CartItemDto
import com.juank.shopapp.data.repositoryImpl.shoppingCart.ShoppingCartResults
import kotlinx.coroutines.flow.Flow

interface ShoppingCartRepository {
    suspend fun getCartItems(): Flow<ShoppingCartResults>

    suspend fun updateCartItem(itemCart: CartItemDto): Flow<ShoppingCartResults>

    suspend fun deleteCartItem(itemCart: CartItemDto): Flow<ShoppingCartResults>
}
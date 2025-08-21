package com.juank.shopapp.domain.useCase.shoppingCart

import com.juank.shopapp.data.mappers.dto.CartItemDto
import com.juank.shopapp.data.repositoryImpl.shoppingCart.ShoppingCartResults
import com.juank.shopapp.domain.repository.shoppingCart.ShoppingCartRepository
import kotlinx.coroutines.flow.Flow

class ShoppingCartUseCase(private val shoppingCartRepository: ShoppingCartRepository) {

    suspend fun getCartItems() : Flow<ShoppingCartResults>{
        return shoppingCartRepository.getCartItems()
    }

    suspend fun updateCartItem(itemCart: CartItemDto) : Flow<ShoppingCartResults> {
        return shoppingCartRepository.updateCartItem(itemCart)
    }

    suspend fun deleteCartItem(itemCart: CartItemDto) : Flow<ShoppingCartResults> {
        return shoppingCartRepository.deleteCartItem(itemCart)
    }
}
package com.juank.shopapp.data.repositoryImpl.shoppingCart

import com.juank.shopapp.data.mappers.dto.CartItemDto
import com.juank.shopapp.data.mappers.dto.toEntity
import com.juank.shopapp.domain.repository.shoppingCart.ShoppingCartRepository
import com.juank.shopapp.domain.utils.AppDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ShoppingCartRepositoryImpl(private val appDao: AppDao): ShoppingCartRepository {

    override suspend fun getCartItems(): Flow<ShoppingCartResults> {
        return flow {
            appDao.cartItemsDao.getAllCartItems().collect { result ->
                emit(ShoppingCartResults.CartItems(result))
            }
        }
    }

    override suspend fun updateCartItem(itemCart: CartItemDto): Flow<ShoppingCartResults> {
        return flow {
            appDao.cartItemsDao.updateCartItem(itemCart.toEntity().quantity, itemCart.toEntity().productId)
            emit(ShoppingCartResults.UpdatedItem(true))
        }
    }

    override suspend fun deleteCartItem(itemCart: CartItemDto): Flow<ShoppingCartResults> {
        return flow {
            appDao.cartItemsDao.deleteCartItem(itemCart.toEntity().productId)
            emit(ShoppingCartResults.IsDeleted(true))
        }
    }
}
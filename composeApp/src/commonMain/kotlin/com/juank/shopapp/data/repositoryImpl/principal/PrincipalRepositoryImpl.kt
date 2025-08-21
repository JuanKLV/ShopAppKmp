package com.juank.shopapp.data.repositoryImpl.principal

import com.juank.shopapp.data.local.database.tables.cartItems.CartItemsTable
import com.juank.shopapp.data.mappers.dto.CartItemDto
import com.juank.shopapp.data.mappers.dto.toEntity
import com.juank.shopapp.data.remote.services.auth.AuthService
import com.juank.shopapp.domain.repository.principal.PrincipalRepository
import com.juank.shopapp.domain.utils.AppDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class PrincipalRepositoryImpl(private val appDao: AppDao, private val authService: AuthService): PrincipalRepository {
    override suspend fun getCategories(): Flow<PrincipalResults> {
        return flow {
            val categories = appDao.categoriesDao.getCategories()
            emit(PrincipalResults.CategoriesList(categories))
        }
    }

    override suspend fun getProducts(): Flow<PrincipalResults> {
        return flow {
            val products = appDao.productsDao.getProducts()
            emit(PrincipalResults.ProductsList(products))
        }
    }

    override suspend fun signOut(): Flow<PrincipalResults> {
        return flow {
            try {
                authService.signOut()
                emit(PrincipalResults.SignOut(isLogout = true))
            } catch (e: Exception) {
                emit(PrincipalResults.Error(e.message ?: "Error al salir."))
            }
        }
    }

    override suspend fun addToCart(cartItems: CartItemDto): Flow<PrincipalResults> {
        return flow {
            val result = appDao.cartItemsDao.getAllCartItems().first()

            val exist = result.find { it.productId == cartItems.product.productId }
            if (exist != null) {
                val updated = exist.copy(quantity = exist.quantity + 1)
                appDao.cartItemsDao.updateCartItem(updated.quantity, updated.productId)
                emit(PrincipalResults.UpdatedItem(true))
            } else {
                appDao.cartItemsDao.insertCartItem(cartItems.toEntity())
                emit(PrincipalResults.InsertItem(true))
            }
        }
    }

    override suspend fun getCartItems(): Flow<PrincipalResults> {
        return flow {
            appDao.cartItemsDao.getAllCartItems().collect { result ->
                emit(PrincipalResults.CartItems(result))
            }
        }
    }

    override suspend fun clearCart(): Flow<PrincipalResults> {
        return flow {
            appDao.cartItemsDao.clearCart()
            emit(PrincipalResults.IsDeleted(true))
        }
    }
}
package com.juank.shopapp.data.repositoryImpl.shoppingCart

import com.juank.shopapp.data.local.database.tables.cartItems.CartItemsTable

sealed class ShoppingCartResults {
    data class CartItems(val cartItemsList: List<CartItemsTable>) : ShoppingCartResults()
    data class UpdatedItem(val isUpdated: Boolean) : ShoppingCartResults()
    data class IsDeleted(val deleted: Boolean) : ShoppingCartResults()
}
package com.juank.shopapp.data.repositoryImpl.principal

import com.juank.shopapp.data.local.database.tables.cartItems.CartItemsTable
import com.juank.shopapp.data.local.database.tables.categories.Categories
import com.juank.shopapp.data.local.database.tables.products.Products

sealed class PrincipalResults {
    data class ProductsList(val productsList: List<Products>): PrincipalResults()
    data class CategoriesList(val categoriesList: List<Categories>): PrincipalResults()
    data class SignOut(val isLogout: Boolean) : PrincipalResults()
    data class Error(val errorMessage: String) : PrincipalResults()
    data class UpdatedItem(val isEdited: Boolean) : PrincipalResults()
    data class InsertItem(val isInserted: Boolean) : PrincipalResults()
    data class CartItems(val cartItemsList: List<CartItemsTable>) : PrincipalResults()
    data class IsDeleted(val isDeleted: Boolean) : PrincipalResults()
}
package com.juank.shopapp.domain.utils

import com.juank.shopapp.data.local.database.tables.cartItems.CartItemsDao
import com.juank.shopapp.data.local.database.tables.categories.CategoriesDao
import com.juank.shopapp.data.local.database.tables.products.ProductsDao

data class AppDao(
    val productsDao: ProductsDao,
    val categoriesDao: CategoriesDao,
    val cartItemsDao: CartItemsDao
)
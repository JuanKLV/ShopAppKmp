package com.juank.shopapp.domain.utils

import com.juank.shopapp.data.local.database.tables.products.ProductsDao

data class AppDao(
    val productsDao: ProductsDao
)
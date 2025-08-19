package com.juank.shopapp.data.local.database.tables.products

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Products")
data class Products(
    @PrimaryKey val productId: Int = 0
)
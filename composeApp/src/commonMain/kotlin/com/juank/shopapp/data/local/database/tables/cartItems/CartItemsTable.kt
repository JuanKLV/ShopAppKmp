package com.juank.shopapp.data.local.database.tables.cartItems

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "CartItems", indices = [Index("productId")])
data class CartItemsTable(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int = 0,
    val productName: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0,
    val image: String = ""
)

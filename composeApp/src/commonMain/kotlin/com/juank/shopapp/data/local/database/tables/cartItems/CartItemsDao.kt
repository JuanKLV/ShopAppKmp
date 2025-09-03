package com.juank.shopapp.data.local.database.tables.cartItems

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemsDao {
    @Query("SELECT * FROM CartItems")
    fun getAllCartItems(): Flow<List<CartItemsTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemsTable)

    @Query("UPDATE CartItems SET quantity=:quantity WHERE productId=:productId ")
    suspend fun updateCartItem(quantity: Int, productId: Int)

    @Query("DELETE FROM CartItems WHERE productId=:productId")
    suspend fun deleteCartItem(productId: Int)

    @Query("DELETE FROM CartItems")
    suspend fun clearCart()
}
package com.juank.shopapp.data.mappers.dto

import com.juank.shopapp.data.local.database.tables.cartItems.CartItemsTable
import com.juank.shopapp.data.local.database.tables.products.Products

data class CartItemDto(
    val product: Products = Products(),
    val quantity: Int = 0
)

fun CartItemDto.toEntity(): CartItemsTable {
    return CartItemsTable(
        productId = product.productId,
        productName = product.productName,
        price = product.price,
        quantity = quantity,
        image = product.image
    )
}

fun CartItemsTable.toDto(): CartItemDto {
    return CartItemDto(
        product = Products(
            productId = productId,
            productName = productName,
            price = price,
            categoryId = 0,
            image = image
        ),
        quantity = quantity
    )
}

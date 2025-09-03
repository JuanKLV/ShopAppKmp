package com.juank.shopapp.presentation.screens.shoppingCart

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.juank.shopapp.data.local.database.tables.products.Products
import com.juank.shopapp.data.mappers.dto.CartItemDto
import com.juank.shopapp.data.repositoryImpl.shoppingCart.ShoppingCartResults
import com.juank.shopapp.domain.useCase.shoppingCart.ShoppingCartUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import shopapp.composeapp.generated.resources.Res
import shopapp.composeapp.generated.resources.bolso
import shopapp.composeapp.generated.resources.camiseta
import shopapp.composeapp.generated.resources.gorra
import shopapp.composeapp.generated.resources.zapatillas

data class ShoppingCartUiState(
    val isError: Boolean = false,
    val errorMessage: String = "",
    val cartItems: List<CartItemDto> = emptyList(),
    val subtotal: Double = 0.0,
    val delivery: Double = 5000.0,
    val taxes: Double = 2300.0,
    val total: Double = 0.0,
    val updated: Boolean = false,
    val deleted: Boolean = false
)

class ShoppingCartViewModel(private val shoppingCartUseCase: ShoppingCartUseCase): ScreenModel {

    private var _uiState = MutableStateFlow(ShoppingCartUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCartFromDb()
    }

    private fun loadCartFromDb() {
        screenModelScope.launch {
            shoppingCartUseCase.getCartItems().collect { results ->
                when(results) {
                    is ShoppingCartResults.CartItems -> {
                        _uiState.update { item ->
                            val mapped = results.cartItemsList.map { e ->
                                CartItemDto(
                                    product = Products(
                                        productId = e.productId,
                                        productName = e.productName,
                                        price = e.price,
                                        categoryId = 0,
                                        image = e.image
                                    ),
                                    quantity = e.quantity
                                )
                            }

                            val subtotal = mapped.sumOf { it.product.price * it.quantity }
                            val total = subtotal + _uiState.value.delivery + _uiState.value.taxes

                            item.copy(
                                cartItems = mapped,
                                subtotal = subtotal,
                                total = total
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun increaseQuantity(item: CartItemDto) {
        screenModelScope.launch {
            val newItem = item.copy(quantity = item.quantity + 1)
            shoppingCartUseCase.updateCartItem(newItem).collect { results ->
                when(results) {
                    is ShoppingCartResults.UpdatedItem -> {
                        _uiState.update {
                            it.copy(updated = results.isUpdated)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun decreaseQuantity(item: CartItemDto) {
        if (item.quantity > 1) {
            screenModelScope.launch {
                val newItem = item.copy(quantity = item.quantity - 1)
                shoppingCartUseCase.updateCartItem(newItem).collect { results ->
                    when(results) {
                        is ShoppingCartResults.UpdatedItem -> {
                            _uiState.update {
                                it.copy(updated = results.isUpdated)
                            }
                        }
                        else -> {}
                    }
                }
            }
        } else {
            screenModelScope.launch {
                shoppingCartUseCase.deleteCartItem(item).collect { results ->
                    when(results) {
                        is ShoppingCartResults.IsDeleted -> {
                            _uiState.update {
                                it.copy(deleted = results.deleted)
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    fun getDrawableResId(fileName: String): DrawableResource {
        return when (fileName) {
            "camiseta.png" -> Res.drawable.camiseta
            "gorra.png" -> Res.drawable.gorra
            "zapatillas.png" -> Res.drawable.zapatillas
            "bolso.png" -> Res.drawable.bolso
            else -> Res.drawable.camiseta
        }
    }
}
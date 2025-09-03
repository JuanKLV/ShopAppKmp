package com.juank.shopapp.presentation.screens.principal

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.juank.shopapp.data.local.database.tables.categories.Categories
import com.juank.shopapp.data.local.database.tables.products.Products
import com.juank.shopapp.data.mappers.dto.CartItemDto
import com.juank.shopapp.data.repositoryImpl.principal.PrincipalResults
import com.juank.shopapp.domain.useCase.principal.PrincipalUseCase
import com.juank.shopapp.presentation.screens.routes.Routes
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

data class PrincipalUiState(
    val productList: List<Products> = emptyList(),
    val allProducts: List<Products> = emptyList(),
    val categoriesList: List<Categories> =  emptyList(),
    val selectedCategory: Int = 1,
    val search: String = "",
    val isError: Boolean = false,
    val errorMessage: String = "",
    val cartItems: List<CartItemDto> = emptyList(),
    val isDeleted: Boolean = false,
    val updatedItem: Boolean = false,
    val insertItem: Boolean = false
)

class PrincipalViewModel(private val principalUseCase: PrincipalUseCase): ScreenModel {

    private var _uiState = MutableStateFlow(PrincipalUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getProducts()
        getCategories()
        loadCartFromDb()
    }

    fun setSearch(value: String) {
        _uiState.update {
            it.copy(search = value)
        }
        applyFilters()
    }

    fun setNewSelectedCategory(value: Int) {
        _uiState.update {
            it.copy(selectedCategory = value)
        }
        applyFilters()
    }

    fun setError(isError: Boolean, message: String) {
        _uiState.update {
            it.copy(isError = isError, errorMessage = message)
        }
    }

    fun getProducts() {
        screenModelScope.launch {
            principalUseCase.getProducts().collect { result ->
                when (result) {
                    is PrincipalResults.ProductsList -> {
                        _uiState.update {
                            it.copy(productList = result.productsList, allProducts = result.productsList)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun applyFilters() {
        val current = _uiState.value
        val filtered = current.allProducts.filter { p ->
            val matchesSearch = p.productName.contains(current.search, ignoreCase = true)
            val matchesCategory = current.selectedCategory == 1 ||
                    p.categoryId == current.selectedCategory
            matchesSearch && matchesCategory
        }
        _uiState.update { it.copy(productList = filtered) }
    }

    fun getCategories() {
        screenModelScope.launch {
            principalUseCase.getCategories().collect { result ->
                when (result) {
                    is PrincipalResults.CategoriesList -> {
                        _uiState.update {
                            it.copy(categoriesList = result.categoriesList)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun signOut(navigator: Navigator?) {
        screenModelScope.launch {
            principalUseCase.signOut().collect { results ->
                when (results) {
                    is PrincipalResults.SignOut -> {
                        navigator?.replaceAll(Routes.LOGIN_SCREEN)
                        principalUseCase.clearCart().collect { i ->
                            when(i) {
                                is PrincipalResults.IsDeleted -> {
                                    _uiState.update {
                                        it.copy(isDeleted = i.isDeleted)
                                    }
                                }
                                else -> {}
                            }
                        }
                    }
                    is PrincipalResults.Error -> {
                        _uiState.update {
                            it.copy(errorMessage = results.errorMessage, isError = true, cartItems = listOf())
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun addToCart(product: Products) {
        val cart = _uiState.value.cartItems.toMutableList()
        val exist = cart.find { it.product.productId == product.productId }

        val updatedItem = if (exist != null) {
            val updated = exist.copy(quantity = exist.quantity + 1)
            cart[cart.indexOf(exist)] = updated
            updated
        } else {
            val newItem = CartItemDto(product, 1)
            cart.add(newItem)
            newItem
        }

        _uiState.update { it.copy(cartItems = cart) }


        screenModelScope.launch {
            principalUseCase.addToCart(updatedItem).collect { results ->
                when(results) {
                    is PrincipalResults.UpdatedItem -> {
                        _uiState.update {
                            it.copy(updatedItem = results.isEdited)
                        }
                    }
                    is PrincipalResults.InsertItem -> {
                        _uiState.update { it.copy(insertItem = results.isInserted) }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun loadCartFromDb() {
        screenModelScope.launch {
            principalUseCase.getCartItems().collect { results ->
                when(results) {
                    is PrincipalResults.CartItems -> {
                        _uiState.update { it.copy(cartItems = results.cartItemsList.map { e ->
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
                        }) }
                    }
                    else -> {}
                }
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.update {
            it.copy(isError = false, errorMessage = "")
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
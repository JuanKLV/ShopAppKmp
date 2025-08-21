package com.juank.shopapp.presentation.screens.shoppingCart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.juank.shopapp.data.mappers.dto.CartItemDto
import com.juank.shopapp.presentation.screens.principal.BottomComposable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import shopapp.composeapp.generated.resources.Res
import shopapp.composeapp.generated.resources.cart
import shopapp.composeapp.generated.resources.delivery
import shopapp.composeapp.generated.resources.ic_add
import shopapp.composeapp.generated.resources.ic_arrow_back
import shopapp.composeapp.generated.resources.ic_remove
import shopapp.composeapp.generated.resources.proceed_to_confirm
import shopapp.composeapp.generated.resources.subtotal
import shopapp.composeapp.generated.resources.taxes
import shopapp.composeapp.generated.resources.total

class ShoppingCartScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        val shoppingCartViewModel = koinInject<ShoppingCartViewModel>()
        val uiState = shoppingCartViewModel.uiState.collectAsState().value

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(stringResource(Res.string.cart), fontWeight = FontWeight.Bold)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator?.pop() }) {
                            Icon(painterResource(Res.drawable.ic_arrow_back), contentDescription = "Back")
                        }
                    }
                )
            },
            bottomBar = {
                BottomComposable()
            }
        ) { padding ->
            MainContentCart(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                shoppingCartViewModel,
                uiState)
        }
    }

    @Composable
    fun MainContentCart(modifier: Modifier, shoppingCartViewModel: ShoppingCartViewModel, uiState: ShoppingCartUiState) {
        Column(
            modifier = modifier
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(uiState.cartItems) { item ->
                    CartItemRow(item, shoppingCartViewModel)
                }
            }

            Spacer(Modifier.height(16.dp))
            PriceRow(stringResource(Res.string.subtotal), "$${uiState.subtotal}")
            PriceRow(stringResource(Res.string.delivery), "$${uiState.delivery}")
            PriceRow(stringResource(Res.string.taxes), "$${uiState.taxes}")
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            PriceRow(stringResource(Res.string.total), "$${uiState.total}", bold = true)

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {  },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(Res.string.proceed_to_confirm))
            }
        }
    }

    @Composable
    fun CartItemRow(item: CartItemDto, shoppingCartViewModel: ShoppingCartViewModel) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(shoppingCartViewModel.getDrawableResId(item.product.image)),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(item.product.productName, fontWeight = FontWeight.Bold)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { shoppingCartViewModel.decreaseQuantity(item) }) {
                    Icon(painterResource(Res.drawable.ic_remove), contentDescription = "Remove")
                }
                Text(item.quantity.toString())
                IconButton(onClick = { shoppingCartViewModel.increaseQuantity(item) }) {
                    Icon(painterResource(Res.drawable.ic_add), contentDescription = "Add")
                }
            }
        }
    }

    @Composable
    fun PriceRow(label: String, value: String, bold: Boolean = false) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
            Text(value, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
        }
    }
}
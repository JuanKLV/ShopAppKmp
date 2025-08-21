package com.juank.shopapp.presentation.screens.principal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.juank.shopapp.data.local.database.tables.products.Products
import com.juank.shopapp.presentation.screens.routes.Routes
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import shopapp.composeapp.generated.resources.Res
import shopapp.composeapp.generated.resources.cart
import shopapp.composeapp.generated.resources.categories
import shopapp.composeapp.generated.resources.home
import shopapp.composeapp.generated.resources.ic_arrow_back
import shopapp.composeapp.generated.resources.ic_cart
import shopapp.composeapp.generated.resources.ic_home
import shopapp.composeapp.generated.resources.ic_list
import shopapp.composeapp.generated.resources.ic_profile
import shopapp.composeapp.generated.resources.products
import shopapp.composeapp.generated.resources.products_top
import shopapp.composeapp.generated.resources.profile
import shopapp.composeapp.generated.resources.search_products
import shopapp.composeapp.generated.resources.shop

class PrincipalScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        val principalViewModel = koinInject<PrincipalViewModel>()
        val uiState = principalViewModel.uiState.collectAsState().value

        val snackBarHostState = remember { SnackbarHostState() }

        LaunchedEffect(uiState.isError) {
            if (uiState.isError && uiState.errorMessage.isNotBlank()) {
                snackBarHostState.showSnackbar(uiState.errorMessage)
                principalViewModel.clearErrorMessage()
            }
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            topBar = {
                TopAppBarMain(principalViewModel, navigator, uiState)
            },
            bottomBar = {
                BottomComposable()
            }
        ) { paddingValues ->
            ContentMain(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp),
                uiState = uiState,
                principalViewModel = principalViewModel
            )
        }
    }

    @Composable
    fun ContentMain(modifier: Modifier, uiState: PrincipalUiState, principalViewModel: PrincipalViewModel) {
        Column(
            modifier = modifier
        ) {
            OutlinedTextField(
                value = uiState.search,
                onValueChange = { principalViewModel.setSearch(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(Res.string.search_products)) },
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            Text(stringResource(Res.string.categories), fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                uiState.categoriesList.forEach { categoria ->
                    FilterChip(
                        selected = uiState.selectedCategory == categoria.categoryId,
                        onClick = { principalViewModel.setNewSelectedCategory(categoria.categoryId) },
                        label = { Text(categoria.categoryName) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(stringResource(Res.string.products_top), fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Spacer(Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.productList) { product ->
                    ProductCard(product, principalViewModel)
                }
            }
        }
    }

    @Composable
    fun ProductCard(product: Products, principalViewModel: PrincipalViewModel) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { principalViewModel.addToCart(product) }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Image(
                    painter = painterResource(principalViewModel.getDrawableResId(product.image)),
                    contentDescription = product.productName,
                    modifier = Modifier
                        .size(120.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(8.dp))
                Text(product.productName, fontWeight = FontWeight.Medium)
                Text("$${product.price}", color = Color.Gray)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopAppBarMain(principalViewModel: PrincipalViewModel, navigator: Navigator?, uiState: PrincipalUiState) {
        CenterAlignedTopAppBar(
            title = { Text(stringResource(Res.string.shop), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center) },
            navigationIcon = {
                IconButton(onClick = { principalViewModel.signOut(navigator) }) {
                    Icon(painterResource(Res.drawable.ic_arrow_back), contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { if (uiState.cartItems.isEmpty()) principalViewModel.setError(true, "No hay productos en el carrito.") else navigator?.push(Routes.SHOPPING_CART_SCREEN) }) {
                    Box {
                        Icon(painterResource(Res.drawable.ic_cart), contentDescription = "Cart", modifier = Modifier.size(30.dp))
                        if (uiState.cartItems.isNotEmpty()) {
                            Badge(containerColor = Color.Red, modifier = Modifier.align(Alignment.TopEnd)
                                .height(10.dp).width(10.dp))
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun BottomComposable() {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = {},
            label = { Text(stringResource(Res.string.home)) },
            icon = { Icon(painterResource(Res.drawable.ic_home), null, modifier = Modifier.size(20.dp)) }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            label = { Text(stringResource(Res.string.products)) },
            icon = { Icon(painterResource(Res.drawable.ic_list), null, modifier = Modifier.size(20.dp)) }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            label = { Text(stringResource(Res.string.cart)) },
            icon = { Icon(painterResource(Res.drawable.ic_cart), null, modifier = Modifier.size(20.dp)) }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            label = { Text(stringResource(Res.string.profile)) },
            icon = { Icon(painterResource(Res.drawable.ic_profile), null, modifier = Modifier.size(20.dp)) }
        )
    }
}

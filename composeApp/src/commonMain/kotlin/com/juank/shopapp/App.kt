package com.juank.shopapp

import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import com.juank.shopapp.presentation.screens.routes.Routes
import com.juank.shopapp.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        Navigator(Routes.LOGIN_SCREEN)
    }
}
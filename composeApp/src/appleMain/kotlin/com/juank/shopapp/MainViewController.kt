package com.juank.shopapp

import androidx.compose.ui.window.ComposeUIViewController
import com.juank.shopapp.data.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }
package com.juank.shopapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
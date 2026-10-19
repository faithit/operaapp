package com.opera.firstapp.models

data class CartItem(
    var id: String = "",
    var productId: String = "",
    var name: String = "",
    var price: String = "",
    var imageUrl: String = "",
    var quantity: Int = 1
)
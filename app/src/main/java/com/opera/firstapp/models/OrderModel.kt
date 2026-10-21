package com.opera.firstapp.models

data class OrderItem(
    var productId: String = "",
    var name: String = "",
    var price: String = "",
    var imageUrl: String = "",
    var quantity: Int = 1
)

data class Order(
    var id: String = "",
    var userId: String = "",
    var items: List<OrderItem> = emptyList(),
    var totalPrice: Double = 0.0,
    var phoneNumber: String = "",
    var status: String = "Paid",
    var timestamp: Long = System.currentTimeMillis()
)
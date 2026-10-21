package com.opera.firstapp.viewModel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.opera.firstapp.models.CartItem
import com.opera.firstapp.models.Order
import com.opera.firstapp.models.OrderItem

class OrderViewModel(var navController: NavHostController, var context: Context) {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val userId = currentUser?.uid ?: "guest"
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Orders").child(userId)

    fun createOrder(
        cartItems: List<CartItem>,
        totalPrice: Double,
        phoneNumber: String,
        onComplete: (Boolean) -> Unit
    ) {
        val ref = databaseReference.push()
        val orderId = ref.key ?: ""

        val orderItems = cartItems.map {
            OrderItem(
                productId = it.productId,
                name = it.name,
                price = it.price,
                imageUrl = it.imageUrl,
                quantity = it.quantity
            )
        }

        val order = Order(
            id = orderId,
            userId = userId,
            items = orderItems,
            totalPrice = totalPrice,
            phoneNumber = phoneNumber,
            status = "Paid",
            timestamp = System.currentTimeMillis()
        )

        ref.setValue(order).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Order created successfully!", Toast.LENGTH_SHORT).show()
                onComplete(true)
            } else {
                Toast.makeText(context, "Failed to save order: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                onComplete(false)
            }
        }
    }

    fun fetchUserOrders(ordersList: SnapshotStateList<Order>) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ordersList.clear()
                val tempOrders = mutableListOf<Order>()
                for (snap in snapshot.children) {
                    val order = snap.getValue(Order::class.java)
                    if (order != null) {
                        tempOrders.add(order)
                    }
                }
                tempOrders.sortByDescending { it.timestamp }
                ordersList.addAll(tempOrders)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
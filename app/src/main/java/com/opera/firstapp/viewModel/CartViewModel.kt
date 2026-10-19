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
import com.opera.firstapp.models.Product

class CartViewModel(var navController: NavHostController, var context: Context) {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val userId = currentUser?.uid ?: "guest"
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child(userId)

    fun addToCart(product: Product) {
        if (currentUser == null) {
            Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show()
            return
        }

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var itemExists = false
                var existingKey = ""
                var currentQuantity = 1

                for (snap in snapshot.children) {
                    val item = snap.getValue(CartItem::class.java)
                    if (item != null && item.productId == product.id) {
                        itemExists = true
                        existingKey = snap.key ?: ""
                        currentQuantity = item.quantity
                        break
                    }
                }

                if (itemExists && existingKey.isNotEmpty()) {
                    databaseReference.child(existingKey).child("quantity").setValue(currentQuantity + 1)
                        .addOnSuccessListener {
                            Toast.makeText(context, "${product.name} quantity updated", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    val cartRef = databaseReference.push()
                    val cartItemId = cartRef.key ?: ""
                    val cartItem = CartItem(
                        id = cartItemId,
                        productId = product.id,
                        name = product.name,
                        price = product.price,
                        imageUrl = product.imageUrl,
                        quantity = 1
                    )
                    cartRef.setValue(cartItem).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to add: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun fetchCartItems(cartItems: SnapshotStateList<CartItem>) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartItems.clear()
                for (snap in snapshot.children) {
                    val item = snap.getValue(CartItem::class.java)
                    if (item != null) {
                        cartItems.add(item)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateQuantity(cartItemId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(cartItemId)
        } else {
            databaseReference.child(cartItemId).child("quantity").setValue(newQuantity)
        }
    }

    fun removeFromCart(cartItemId: String) {
        databaseReference.child(cartItemId).removeValue().addOnSuccessListener {
            Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show()
        }
    }

    fun clearCart() {
        databaseReference.removeValue().addOnSuccessListener {
            Toast.makeText(context, "Cart cleared", Toast.LENGTH_SHORT).show()
        }
    }
}
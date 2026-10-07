package com.opera.firstapp.viewModel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.FirebaseDatabaseKtxRegistrar
import com.opera.firstapp.navigation.ROUTE_PRODUCTLIST
import com.opera.firstapp.network.CloudinaryAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream

class ProductViewModel(var navController: NavHostController,var context: Context){
    val cloudinaryUrl="https://api.cloudinary.com/v1_1/dojp0mlml/upload" //replace dojp0mlml with your cloud name
    val uploadPreset="operaproducts"
    val  databasereference= FirebaseDatabase.getInstance().getReference("Products")
    //functions
    //crud-create,r-read,u-update,d-delete
    //upload product to firebase realtime database
    fun addProduct(name: String,price: String,description: String,imageUri: Uri?){
        val ref=databasereference.push()
        val currentUser= FirebaseAuth.getInstance().currentUser
        val userId=currentUser?.uid ?: ""
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val imageUrl=if(imageUri!=null){
                 uploadToCloudinary(context = context, uri = imageUri)
                }else{
                    ""
                }
                // product data to be stored in realtime db
                val productData=mapOf(
                    "id" to ref.key,
                    "name" to name,
                    "price" to price,
                    "description" to description,
                    "userId" to userId,
                    "imageUrl" to imageUrl
                )
                ref.setValue(productData).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(context,"product added successfully", Toast.LENGTH_LONG).show()
                        //navigate to productlist
                        navController.navigate(ROUTE_PRODUCTLIST)

                    }
                    else{
                        Toast.makeText(context,"Error: ${it.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }catch(e: Exception){
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Upload failed ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    //upload  image to cloudinary function

    private suspend fun uploadToCloudinary(context: Context, uri: Uri): String {
        // Get the selected image
        val inputStream = context.contentResolver.openInputStream(uri) ?: throw Exception("Image read failed")
        // Convert image to bytes
        val fileBytes = inputStream.readBytes()
        // Create image request body
        val requestBody = fileBytes.toRequestBody("image/*".toMediaType())
        // Create multipart file
        val filePart = MultipartBody.Part.createFormData("file", "image.jpg", requestBody)
        // Create upload preset
        val preset = uploadPreset.toRequestBody("text/plain".toMediaType())
        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.cloudinary.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // Create Cloudinary API
        val cloudinaryAPI = retrofit.create(
            CloudinaryAPI::class.java
        )
        // Make the Cloudinary API call
        val response = cloudinaryAPI.uploadImage(filePart, preset)
        // Check response
        if (response.isSuccessful) {
            // Get Cloudinary image URL
            return response.body()?.secure_url
                ?: throw Exception("Image URL not found")
        } else {
            throw Exception(
                "Cloudinary upload failed: ${response.message()}"
            )
        }
    }
    //r-READ products from db
    //fetch all products from realtome database
    fun allProducts(){


    }
    //u-update
    //update existing product in firebase
    fun updateProduct(){

    }
    //d-delete
    //delete  product in realtime databse
    fun deleteProduct(){

    }




}
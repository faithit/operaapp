package com.opera.firstapp.viewModel

import android.content.Context
import android.net.Uri
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.FirebaseDatabaseKtxRegistrar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
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

            }catch(e: Exception){

            }

        }


    }
    //upload  image to cloudinary function
    private fun uploadToCloudinary(context: Context, uri: Uri): String {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val fileBytes = inputStream?.readBytes()
            ?: throw Exception("Image read failed")
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                "image.jpg",
                RequestBody.create("image/*".toMediaTypeOrNull(), fileBytes)
            )
            .addFormDataPart("upload_preset", uploadPreset)
            .build()
        val request = Request.Builder()
            .url(cloudinaryUrl)
            .post(requestBody)
            .build()
        val response = OkHttpClient().newCall(request).execute()
        if (!response.isSuccessful) throw Exception("Upload failed")
        val responseBody = response.body?.string()
        val secureUrl = Regex("\"secure_url\":\"(.*?)\"")
            .find(responseBody ?: "")?.groupValues?.get(1)
        return secureUrl ?: throw Exception("Failed to get image URL")
    }


}
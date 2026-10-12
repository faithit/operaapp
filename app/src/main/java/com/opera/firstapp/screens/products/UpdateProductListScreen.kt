package com.opera.firstapp.screens.products

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.opera.firstapp.models.Product
import com.opera.firstapp.viewModel.ProductViewModel

//add screen and preview
//add outlined textfields for name,price,decription
//add asyncimage and button for imagepicker
//update product button
 @OptIn(ExperimentalMaterial3Api::class)
 @Composable
fun UpdateProductScreen(navController: NavHostController,productId: String){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Update Product") },
                colors= TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Green,
                    titleContentColor =Color.Blue
                )
            )
        }
    ) { innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            var productName by remember { mutableStateOf("") }
            var price by remember { mutableStateOf("") }
            var description by remember { mutableStateOf("") }
            var imageUri by remember { mutableStateOf<Uri?>(null) }
            var imageUrl by remember { mutableStateOf<String?>(null) }
            //imagepicker
            val imagePickerLauncher= rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ){
                uri:Uri? ->
                if (uri!=null)imageUri=uri
            }
            val context= LocalContext.current
            val myproductviewmodel= ProductViewModel(navController,context)
            //fetch productt data
            LaunchedEffect(productId) {
                myproductviewmodel.databasereference.child(productId).get()
                    .addOnSuccessListener {
                        snapshot ->
                        val product=snapshot.getValue(Product::class.java)
                        product ?. let{
                            productName=it.name
                            price =it.price
                            description=it.description
                            imageUrl=it.imageUrl
                        }
                    }
            }
            //outlined textfield  for product name
            OutlinedTextField(
                value = productName,
                onValueChange = {productName=it},
                label={Text("Product Name")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier= Modifier.height(20.dp))
            OutlinedTextField(
                value = price,
                onValueChange = {price=it},
                label={Text("Product Price")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier= Modifier.height(20.dp))
            OutlinedTextField(
                value = description,
                onValueChange = {description=it},
                label={Text("Product Description")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier= Modifier.height(20.dp))
            //image preview(old or new)
            val imageSource:Any?=imageUri ?:imageUrl
            if (imageSource !=null){
                AsyncImage(
                    model=imageSource,
                    contentDescription = "product",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop

                )
            }
            Spacer(modifier= Modifier.height(20.dp))
            //image picker
            Button(onClick ={imagePickerLauncher.launch("image/*") },
                modifier= Modifier.fillMaxWidth(),) {
                Text("UPDATE IMAGE")
            }
            Spacer(modifier= Modifier.height(20.dp))
            Button(
                onClick = {
                    myproductviewmodel.updateProduct(
                        productId=productId,
                        name = productName,
                        price=price,
                        description = description,
                        imageUri = imageUri
                    )
                },
                modifier= Modifier.fillMaxWidth(),
                colors= ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Blue
                )
            ) {
            Text("UPDATE PRODUCT")
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun updateproductscreenpreview(){
    UpdateProductScreen(rememberNavController(), productId = "2")
}

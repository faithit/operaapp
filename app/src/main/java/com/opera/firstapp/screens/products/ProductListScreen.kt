package com.opera.firstapp.screens.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.opera.firstapp.R
import com.opera.firstapp.models.Product
import com.opera.firstapp.navigation.ROUTE_ADDPRODUCT
import com.opera.firstapp.navigation.ROUTE_PRODUCTLIST
import com.opera.firstapp.navigation.ROUTE_UPDATEPRODUCT
import com.opera.firstapp.viewModel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(navController: NavHostController){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
            title =  {Text("PRODUCT LIST",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp)},
                colors= TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Green,
                    titleContentColor = Color.Blue
                )
        )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {navController.navigate(ROUTE_ADDPRODUCT)},
                containerColor = Color.Green
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add icon",
                    tint = Color.Red
                )
            }
        }

    ) {
       innerPadding ->
        val products= remember { mutableStateListOf<Product>() }
        val context=LocalContext.current
        val myproductviewmodel= ProductViewModel(navController,context)
        LaunchedEffect(Unit) {
            myproductviewmodel.allProducts(products)
        }
        //lazy column
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(products){ item ->
                // product card
                Card(
                    shape= RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation =6.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor= Color.White
                    ),
                    modifier= Modifier
                        .fillMaxWidth()
                        .padding(horizontal=8.dp, vertical =4.dp)
              ) {
                    //product image
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = "product image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(180.dp)
                            .fillMaxWidth()
                    )
                    //column
                    Column(modifier=Modifier.padding(16.dp))
                    {
                        Text(text=item.name,
                            color=Color.Magenta,
                            fontSize=28.sp)
                        Text(
                            text=item.description,
                            fontSize = 24.sp
                        )
                        Text(
                            text="Price: Ksh ${item.price}",
                            color=Color.Red,
                            fontSize = 24.sp
                        )
                    }
                    //row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =Arrangement.SpaceBetween

                    ) {
                        Button(onClick = {
                            myproductviewmodel.deleteProduct(item.id)
                        },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            ),
                            modifier = Modifier.weight(1f)) {
                            Text(text="Delete",fontWeight = FontWeight.Bold)
                        }
                        Button(onClick = {
                            navController.navigate("$ROUTE_UPDATEPRODUCT/${item.id}")
                        },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Green
                            ),
                            modifier = Modifier.weight(1f)) {
                            Text(text="Update", fontWeight = FontWeight.Bold)
                        }


                    }




                }


            }


        }


    }

}
@Preview(showBackground = true)
@Composable
fun productlistscreenpreview(){
    ProductListScreen(rememberNavController())
}
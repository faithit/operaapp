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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.opera.firstapp.R
import com.opera.firstapp.models.Product
import com.opera.firstapp.navigation.ROUTE_ADDPRODUCT

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
         val products=listOf(
             Product("1","bag","a nice bag","6777",R.drawable.bag.toString()),
             Product("2","laptop","hp laptop","678888",R.drawable.laptop.toString()) ,
             Product("3","shoes"," sports shoes","5677",R.drawable.shoes.toString()),
             Product("4","phone","smartphone","20000",R.drawable.logo.toString()),

         )
        //lazy column
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal=8.dp, vertical =4.dp)
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
                        .height(300.dp)
                        .padding(bottom=16.dp),

              ) {
                    //product image
                    Image(painter = painterResource(id=item.imageURL.toInt()),
                        contentDescription = "product image",
                        contentScale =  ContentScale.Crop,
                        modifier=Modifier
                            .height(100.dp)
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
                        Button(onClick = {},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            ),
                            modifier = Modifier.weight(1f)) {
                            Text(text="Delete",fontWeight = FontWeight.Bold)
                        }
                        Button(onClick = {},
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
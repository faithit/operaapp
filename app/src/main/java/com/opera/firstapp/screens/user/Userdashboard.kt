package com.opera.firstapp.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.opera.firstapp.R
import com.opera.firstapp.models.Product
import com.opera.firstapp.viewModel.AuthViewModel
import com.opera.firstapp.viewModel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboard(navController: NavHostController){
    var context= LocalContext.current
    var myproductviewmodel= ProductViewModel(navController,context)
    var myauthviewmodel= AuthViewModel(navController,context)
    var username by remember { mutableStateOf("loading..") }
    var products =remember { mutableStateListOf<Product>() }
    //fetch products from firebase and logged in user
    LaunchedEffect(Unit) {
        myproductviewmodel.allProducts(products)
        myauthviewmodel.getCurrentUserName {username=it  }
    }
    //add a scaffold
    Scaffold(
        //add topbar
        topBar = {
            TopAppBar(
                title={Text("Userdashboard")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Green,
                    titleContentColor = Color.Magenta,

                ),
                actions={
                    IconButton(onClick = { myauthviewmodel.signout()}) {
                        Icon(imageVector = Icons.Default.ExitToApp,
                            contentDescription = "logout")
                    }
                }
            )
        },

    //bottom bar
        bottomBar = {
            NavigationBar(
                containerColor = Color.Green
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon ={ Icon(
                        Icons.Default.Home,
                        contentDescription = " homeicon",
                        tint=Color.Magenta)},
                    label = {Text("Home",color= Color(0xFFFF8AFF))}
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon ={ Icon(
                        Icons.Default.Person,
                        contentDescription = " person icon",
                        tint=Color.Magenta
                    )},
                    label = {Text("profile")}
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon ={ Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = " cart icon",
                        tint=Color.Magenta)},
                    label = {Text("Cart")}
                )
            }
        }

    )
    {
        innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding)
                .padding(16.dp)
        ) {
//            val products=listOf(
//                Product("1","bag","a nice bag","890099",R.drawable.bag.toString()),
//                Product("2","laptop","hplaptop","78899",R.drawable.laptop.toString()),
//                Product("3","shoes","running shoes","45666",R.drawable.shoes.toString()),
//                Product("4","chair","premium ","1200",R.drawable.logod.toString())
//            )
            Text(
                text="welcome ${username} ! ",
                color=Color.Blue,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier=Modifier.height(16.dp))
             var searchText by remember { mutableStateOf("") }
            OutlinedTextField(
                value = searchText,
                onValueChange = {searchText=it},
                placeholder = {Text("search products ...")},
                leadingIcon = {Icon(imageVector = Icons.Default.Search,
                    contentDescription = "search")},
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier=Modifier.height(16.dp))
            Text(text="featured products",
                color=Color.Blue,
                style= MaterialTheme.typography.titleLarge)
            Spacer(modifier=Modifier.height(16.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products){product->
                    //product card
                    Card(
                        modifier = Modifier
                            .width(200.dp),
                        shape=RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            //product image
                            Image(
                                painter = rememberAsyncImagePainter(product.imageUrl),
                                contentDescription = product.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                            )
                                //product name
                            Text(text=product.name,
                                fontSize = 18.sp,
                                color=Color.Blue,
                                fontWeight = FontWeight.Bold)
                            //product description
                            Text(text=product.description, fontSize = 14.sp)
                            //product prize
                            Text(text="ksh ${product.price}",color=Color.Red)
                        }

                    }

                }

            }

        }



    }

}
@Preview(showBackground = true)
@Composable
fun userdashboardpreview(){
    UserDashboard(rememberNavController())
}

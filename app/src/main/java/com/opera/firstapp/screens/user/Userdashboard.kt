package com.opera.firstapp.screens.user

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboard(navController: NavHostController){
    //add a scaffold
    Scaffold(
        //add topbar
        topBar = {
            TopAppBar(
                title={Text("Userdashboard")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Green,
                    titleContentColor = Color.Magenta
                ),
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
        paddingValues ->

    }

}
@Preview(showBackground = true)
@Composable
fun userdashboardpreview(){
    UserDashboard(rememberNavController())
}

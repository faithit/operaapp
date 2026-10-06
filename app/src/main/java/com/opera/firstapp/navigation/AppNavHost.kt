package com.opera.firstapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.opera.firstapp.screens.dashboard.DashboardScreen
import com.opera.firstapp.screens.login.LoginScreen
import com.opera.firstapp.screens.onboardingscreen.OnboardingScreen
import com.opera.firstapp.screens.products.AddProductScreen
import com.opera.firstapp.screens.products.ProductListScreen
import com.opera.firstapp.screens.register.RegisterScreen
import com.opera.firstapp.screens.splashscreen.SplashScreen
import com.opera.firstapp.screens.user.UserDashboard

@Composable
fun AppNavHost(
    modifier: Modifier=Modifier,
    navController: NavHostController= rememberNavController(),
    startDestination: String=ROUTE_SPLASH
){
    NavHost(
        navController=navController,
        modifier=modifier,
        startDestination = startDestination
    ){
        composable(ROUTE_SPLASH) {
            SplashScreen(navController)
        }
        composable (ROUTE_LOGIN){
            LoginScreen(navController)
        }
        composable(ROUTE_REGISTER) {
            RegisterScreen(navController)
        }
        composable(ROUTE_DASHBOARD) {
            DashboardScreen(navController)
        }
        composable(ROUTE_ONBOARDING ) {
            OnboardingScreen(navController)
        }
        composable(ROUTE_USERDASHBOARD) {
            UserDashboard(navController)
        }
        composable (ROUTE_ADDPRODUCT){
            AddProductScreen(navController)
        }
        composable(ROUTE_PRODUCTLIST) {
            ProductListScreen(navController)
        }

    }


}